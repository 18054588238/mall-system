package com.personal.mall.seckill.service.impl;

import com.alibaba.fastjson.JSON;
import com.personal.common.constant.SeckillConstant;
import com.personal.common.utils.R;
import com.personal.mall.seckill.dto.SeckillSkuRedisDTO;
import com.personal.mall.seckill.feign.CouponFeignService;
import com.personal.mall.seckill.feign.ProductFeignService;
import com.personal.mall.seckill.service.SkuSeckillService;
import com.personal.mall.seckill.vo.SeckillSkuSessionVO;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SkuSeckillServiceImpl implements SkuSeckillService {

    @Autowired
    private CouponFeignService couponFeignService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ProductFeignService productFeignService;
    @Autowired
    private RedissonClient redissonClient;


    @Override
    public void seckillSkuLatestDays(String[] latestDays) {
        // 通过openfeign调用coupon服务
        R r = couponFeignService.getSeckillSkuLatestDays(latestDays);
        if (r.getCode() == 0) {
            String data = (String) r.get("data");
            List<SeckillSkuSessionVO> seckillSkuSessionVOS = JSON.parseArray(data, SeckillSkuSessionVO.class);
            // 上架商品 -- 保存相关信息到redis中
            saveSeckillInfosToRedis(seckillSkuSessionVOS);
        }
    }

    private void saveSeckillInfosToRedis(List<SeckillSkuSessionVO> seckillSkuSessionVOS) {
        // 1.保存秒杀活动信息，key： start_endTime    value: sessionId_skuId

        // 2.保存秒杀商品的详情 hash存储 key：sessionId_skuId  value：sku相关信息封装的对象
        // boundHashOps方法可以获取一个与特定键绑定的 Hash操作对象
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(SeckillConstant.SKU_CACHE_PREFIX);

        seckillSkuSessionVOS.forEach(item -> {
            String key = SeckillConstant.SESSION_CACHE_PREFIX+item.getStartTime().getTime()+"_"+item.getEndTime().getTime();

            // 判断key是否已存在
            Boolean hasKey = redisTemplate.hasKey(key);
            if (!hasKey) {
                // key不存在
                List<String> value = item.getSeckillSkuRelationList().stream().map(i -> i.getPromotionSessionId().toString() + "_" + i.getSkuId().toString()).collect(Collectors.toList());
                redisTemplate.opsForList().leftPushAll(key, value);
            }

            item.getSeckillSkuRelationList().forEach(i -> {
                String skuKey = i.getPromotionSessionId().toString() + "_" + i.getSkuId().toString();
                Boolean hasKey1 = operations.hasKey(skuKey);
                if (Boolean.FALSE.equals(hasKey1)) {
                    // 存储
                    SeckillSkuRedisDTO redisDTO = new SeckillSkuRedisDTO();

                    redisDTO.setSeckillSkuRelationVO(i);

                    R info = productFeignService.info(i.getSkuId());
                    if (info.getCode() == 0) {
                        String skuInfoJson = (String) info.get("skuInfoJson");
                        // 调用openfeign查询
                        redisDTO.setSkuInfo(JSON.parseObject(skuInfoJson, SeckillSkuRedisDTO.SkuInfoVO.class));
                    }


                    redisDTO.setEndTime(item.getEndTime());
                    redisDTO.setStartTime(item.getStartTime());

                    String randomCode = UUID.randomUUID().toString().replace("-", "");
                    redisDTO.setRandomCode(randomCode);

                    // 分布式信号量的处理  限流的目的（分布式信号量可以用来控制同时访问某个资源的线程数量）
                    RSemaphore semaphore = redissonClient.getSemaphore(SeckillConstant.SKU_STOCK_SEMAPHORE + randomCode);
                    // 把秒杀活动的商品数量作为分布式信号量的信号量
                    // 设置总许可数	初始化库存
                    semaphore.trySetPermits(i.getSeckillCount().intValue());
                    operations.put(skuKey, JSON.toJSONString(redisDTO));
                }
            });

        });

    }
}
