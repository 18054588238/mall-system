package com.personal.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.personal.common.utils.PageUtils;
import com.personal.mall.product.entity.SkuInfoEntity;
import com.personal.mall.product.entity.vo.ItemVO;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * sku信息
 *
 * @author liupanpan
 * @email merdermi902@gmail.com
 * @date 2025-07-28 18:41:30
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageByCondition(Map<String, Object> params);

    ItemVO item(Long skuId) throws ExecutionException, InterruptedException;
}

