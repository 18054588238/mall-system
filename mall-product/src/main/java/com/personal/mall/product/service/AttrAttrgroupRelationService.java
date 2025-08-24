package com.personal.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.personal.common.utils.PageUtils;
import com.personal.mall.product.entity.AttrAttrgroupRelationEntity;
import com.personal.mall.product.entity.vo.AttrGroupWithAttrsVO;
import com.personal.mall.product.entity.vo.AttrRelaDelVO;

import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author liupanpan
 * @email merdermi902@gmail.com
 * @date 2025-07-28 18:41:30
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addRelaAttr(AttrRelaDelVO[] attrRelaDelVO);
}

