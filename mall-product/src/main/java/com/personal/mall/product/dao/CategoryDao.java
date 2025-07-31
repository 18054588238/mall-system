package com.personal.mall.product.dao;

import com.personal.mall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author liupanpan
 * @email merdermi902@gmail.com
 * @date 2025-07-28 18:41:30
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
