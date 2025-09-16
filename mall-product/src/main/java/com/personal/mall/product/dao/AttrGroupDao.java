package com.personal.mall.product.dao;

import com.personal.mall.product.entity.AttrGroupEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.personal.mall.product.entity.vo.SpuItemGroupAttrVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性分组
 * 
 * @author liupanpan
 * @email merdermi902@gmail.com
 * @date 2025-07-28 18:41:30
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {

    List<SpuItemGroupAttrVO> getGroupAttr(@Param("spuId") Long spuId,@Param("catalogId") Long catalogId);
}
