package com.personal.mall.product.dao;

import com.personal.mall.product.entity.SkuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.personal.mall.product.entity.vo.SkuSaleAttrValueVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku信息
 * 
 * @author liupanpan
 * @email merdermi902@gmail.com
 * @date 2025-07-28 18:41:30
 */
@Mapper
public interface SkuInfoDao extends BaseMapper<SkuInfoEntity> {

    List<SkuSaleAttrValueVO> getSaleAttrs(@Param("spuId") Long spuId);
}
