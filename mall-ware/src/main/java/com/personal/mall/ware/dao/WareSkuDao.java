package com.personal.mall.ware.dao;

import com.personal.mall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 商品库存
 * 
 * @author liupanpan
 * @email merdermi902@gmail.com
 * @date 2025-07-29 20:22:11
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    Integer getStock(Long skuId);
}
