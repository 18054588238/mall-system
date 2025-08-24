package com.personal.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.personal.common.utils.PageUtils;
import com.personal.mall.product.entity.AttrEntity;
import com.personal.mall.product.entity.AttrGroupEntity;
import com.personal.mall.product.entity.vo.AttrGroupWithAttrsVO;
import com.personal.mall.product.entity.vo.AttrRelaDelVO;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author liupanpan
 * @email merdermi902@gmail.com
 * @date 2025-07-28 18:41:30
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Long catelogId);

    List<AttrEntity> getRelaAtrr(Long attrgroupId);

    void deleteRelaAtrr(AttrRelaDelVO[] attrRelaDelVO);

    List<AttrGroupWithAttrsVO> getWithattr(Long catalogId);
}

