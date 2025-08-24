package com.personal.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.personal.common.utils.PageUtils;
import com.personal.mall.product.entity.AttrEntity;
import com.personal.mall.product.entity.vo.AttrGroupEntityVO;
import com.personal.mall.product.entity.vo.AttrResponseVO;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author liupanpan
 * @email merdermi902@gmail.com
 * @date 2025-07-28 18:41:30
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAndGroup(AttrGroupEntityVO attr);

    PageUtils queryBasePage(Map<String, Object> params, Long catelogId, String attrType);

    AttrResponseVO getAttrInfo(Long attrId);

    void updateBaseAttr(AttrGroupEntityVO attr);

    void removeByIdsDetails(List<Long> list);

    PageUtils getNoRelaAtrr(Long attrgroupId, Map<String, Object> params);
}

