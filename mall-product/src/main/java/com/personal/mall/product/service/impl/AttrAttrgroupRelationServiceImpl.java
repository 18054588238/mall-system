package com.personal.mall.product.service.impl;

import com.personal.mall.product.entity.vo.AttrGroupWithAttrsVO;
import com.personal.mall.product.entity.vo.AttrRelaDelVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.common.utils.PageUtils;
import com.personal.common.utils.Query;

import com.personal.mall.product.dao.AttrAttrgroupRelationDao;
import com.personal.mall.product.entity.AttrAttrgroupRelationEntity;
import com.personal.mall.product.service.AttrAttrgroupRelationService;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void addRelaAttr(AttrRelaDelVO[] attrRelaDelVO) {
        List<AttrAttrgroupRelationEntity> list = new ArrayList<>();
        Arrays.asList(attrRelaDelVO).forEach(item -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            list.add(relationEntity);
        });
        this.saveBatch(list);
    }

}