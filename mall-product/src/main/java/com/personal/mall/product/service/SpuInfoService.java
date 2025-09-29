package com.personal.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.personal.common.utils.PageUtils;
import com.personal.mall.product.entity.SpuInfoEntity;
import com.personal.mall.product.entity.vo.OrderItemSpuInfoVO;
import com.personal.mall.product.entity.vo.SpuInfoVO;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * spu信息
 *
 * @author liupanpan
 * @email merdermi902@gmail.com
 * @date 2025-07-28 18:41:30
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuInfoVO spuInfo);

    PageUtils queryPageByCondition(Map<String, Object> params);

    void up(Long spuId) throws IOException;

    List<OrderItemSpuInfoVO> getOrderItemSpuInfoBySpuId(Set<Long> spuIds);
}

