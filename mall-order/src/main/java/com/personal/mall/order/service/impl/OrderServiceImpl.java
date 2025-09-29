package com.personal.mall.order.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.personal.common.constant.OrderConstant;
import com.personal.common.exception.WareNoStockException;
import com.personal.common.utils.R;
import com.personal.common.vo.MemberVO;
import com.personal.mall.order.dto.OrderCreateDTO;
import com.personal.mall.order.entity.OrderItemEntity;
import com.personal.mall.order.feign.CartFeignService;
import com.personal.mall.order.feign.MemberServiceFeign;
import com.personal.mall.order.feign.ProductServiceFeign;
import com.personal.mall.order.feign.WareFeignService;
import com.personal.mall.order.interceptor.AuthInterceptor;
import com.personal.mall.order.service.OrderItemService;
import com.personal.mall.order.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.common.utils.PageUtils;
import com.personal.common.utils.Query;

import com.personal.mall.order.dao.OrderDao;
import com.personal.mall.order.entity.OrderEntity;
import com.personal.mall.order.service.OrderService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Autowired
    private MemberServiceFeign memberServiceFeign;
    @Autowired
    private CartFeignService cartFeignService;
    @Autowired
    ThreadPoolExecutor executor;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    private ProductServiceFeign productServiceFeign;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private WareFeignService wareFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVO confirmOrder() {
        OrderConfirmVO confirmVO = new OrderConfirmVO();
        MemberVO memberVO = AuthInterceptor.threadLocal.get();
        Long memberVOId = memberVO.getId();

        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        // 异步编排
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(attributes);
            // 获取当前登录用户的地址信息
            List<MemberAddressVO> addressInfo = memberServiceFeign.getAddressInfo(memberVOId);
            confirmVO.setAddress(addressInfo);
        });
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            // 保存认证信息
            RequestContextHolder.setRequestAttributes(attributes);
            // 获取当前登录用户的购物车信息
            List<OrderItemVO> orderItemVOS = cartFeignService.checkCartList();
            confirmVO.setItems(orderItemVOS);
        });

        try {
            CompletableFuture.allOf(future1,future2).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        /*①客户端请求获取token，服务端生成一个唯一ID作为token存在redis中；
          ②客户端第二次请求时携带token，服务端校验token成功则执行业务操作并删除token，服务端校验token失败则表示重复操作。
        */
        // 生成防重的token，保存到redis，并绑定给前端
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        // 存储到redis中, order:token:用户编号
        redisTemplate.opsForValue().set(OrderConstant.ORDER_TOKEN_PREFIX+":"+memberVOId,token);
        confirmVO.setOrderToken(token);// 响应给前端
        return confirmVO;
    }

    // 添加事务，库存锁定失败时，创建的订单也要撤回(库存服务，暂时用抛异常的方式保证事务)
    @Transactional
    @Override
    public OrderResponseVO orderSubmit(OrderSubmitVO vo) throws WareNoStockException {
        OrderResponseVO responseVO = new OrderResponseVO();
        String orderToken = vo.getOrderToken();
        // 获取当前用户信息
        MemberVO memberVO = AuthInterceptor.threadLocal.get();
        String key = OrderConstant.ORDER_TOKEN_PREFIX + ":" + memberVO.getId();
        // 防重
//        String token = redisTemplate.opsForValue().get(key);
        // 通过脚本实现原子操作(查询和删除的原子性)
        String script = "if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        // token相等，则删除redis数据
        Long l = redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class),
                Arrays.asList(key),
                orderToken);

        if (l == 9) {
            // 表示重复提交
            responseVO.setCode(1);
            return responseVO;
        }
        // 生成订单
        OrderCreateDTO orderCreateDTO = createOrder(vo,memberVO);

        // 保存订单
        this.save(orderCreateDTO.getOrderEntity());


        orderItemService.saveBatch(orderCreateDTO.getOrderItemEntities());
        responseVO.setOrderEntity(orderCreateDTO.getOrderEntity());
        // 锁定库存     需要的信息：每个订单项的 skuid，wareid（暂时不用），需要锁定的数量
        R r = lockWare(orderCreateDTO);
        if (r.getCode() != 0) {
            responseVO.setCode(2);// 表示锁定库存失败
            // 抛异常
            throw new WareNoStockException(100l);
        }
        responseVO.setCode(0);// 创建订单成功
        return responseVO;
    }

    private R lockWare(OrderCreateDTO orderCreateDTO) {
        List<OrderWareLockVO> wareLockVOS = orderCreateDTO.getOrderItemEntities().stream()
                .map(i -> OrderWareLockVO.builder()
                        .skuId(i.getSkuId())
                        .lockCount(i.getSkuQuantity())
                        .build())
                .collect(Collectors.toList());
        return wareFeignService.lockWareStock(wareLockVOS);
    }

    private OrderCreateDTO createOrder(OrderSubmitVO vo,MemberVO memberVO) {

        OrderCreateDTO orderCreateDTO = new OrderCreateDTO();
        // 创建订单
        OrderEntity orderEntity = getOrderCreateDTO(vo, memberVO);
        Long id = orderEntity.getId();
        // 创建订单项(包含订单id)
        List<OrderItemEntity> orderItems = getOrderItemEntity(orderEntity.getOrderSn());

        orderCreateDTO.setOrderEntity(orderEntity);
        orderCreateDTO.setOrderItemEntities(orderItems);

        return orderCreateDTO;
    }

    private List<OrderItemEntity> getOrderItemEntity(String orderSn) {
        List<OrderItemEntity> orderItems = new ArrayList<>();

        // 获取购物车中用户选中的商品信息
        List<OrderItemVO> orderItemVOS = cartFeignService.checkCartList();

        if (orderItemVOS != null&&!orderItemVOS.isEmpty()) {

            Set<Long> spuIdSet = orderItemVOS.stream()
                    .map(OrderItemVO::getSpuId)
                    .collect(Collectors.toSet());
            // 根据spuId查询spu相关信息
            Map<Long, OrderItemSpuInfoVO> itemSpuInfoVOMap = productServiceFeign.getOrderItemSpuInfoBySpuId(spuIdSet).stream()
                    .collect(Collectors.toMap(OrderItemSpuInfoVO::getSpuId, i -> i));

            for (OrderItemVO itemVO : orderItemVOS) {

                OrderItemSpuInfoVO itemSpuInfoVO = itemSpuInfoVOMap.get(itemVO.getSpuId());

                OrderItemEntity orderItemEntity = OrderItemEntity.builder()
//                        .orderId()
                        .orderSn(orderSn)
                        .spuId(itemVO.getSpuId())
                        .spuName(itemSpuInfoVO.getSpuName())
                        .spuPic(itemSpuInfoVO.getSpuPic())
                        .spuBrand(itemSpuInfoVO.getSpuBrandName())
                        .categoryId(itemSpuInfoVO.getCategoryId())
                        .skuId(itemVO.getSkuId())
                        .skuName(itemVO.getTitle())
                        .skuPic(itemVO.getImage())
                        .skuPrice(itemVO.getPrice())
                        .skuQuantity(itemVO.getCount())
                        .skuAttrsVals(StringUtils.collectionToDelimitedString(itemVO.getSkuAttr(), ";"))
                        .giftGrowth(itemVO.getPrice().intValue())
                        .giftIntegration(itemVO.getPrice().intValue())
                        .build();
                orderItems.add(orderItemEntity);
            }
        }

        return orderItems;
    }

    private OrderEntity getOrderCreateDTO(OrderSubmitVO vo, MemberVO memberVO) {

        // 根据地址id获取地址信息
        MemberAddressVO addressVO = memberServiceFeign.getByAddressId(vo.getAddressId());

        // 创建订单编号
        String orderSn = IdWorker.getTimeId();

        return OrderEntity.builder()
                .memberId(memberVO.getId())
                .orderSn(orderSn)
                .memberUsername(memberVO.getUsername())
                .status(OrderConstant.OrderStateEnum.FOR_THE_PAYMENT.getCode())
                .receiverName(addressVO.getName())
                .receiverPhone(addressVO.getPhone())
                .receiverPostCode(addressVO.getPostCode())
                .receiverProvince(addressVO.getProvince())
                .receiverCity(addressVO.getCity())
                .receiverRegion(addressVO.getRegion())
                .receiverDetailAddress(addressVO.getDetailAddress())
                .build();
    }
}