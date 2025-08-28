package com.personal.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.personal.common.utils.PageUtils;
import com.personal.mall.ware.entity.PurchaseDetailEntity;
import com.personal.mall.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author liupanpan
 * @email merdermi902@gmail.com
 * @date 2025-07-29 20:22:11
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(PurchaseDetailEntity detailEntity);

    List<Long> haveStock(List<Long> skuId);
}

