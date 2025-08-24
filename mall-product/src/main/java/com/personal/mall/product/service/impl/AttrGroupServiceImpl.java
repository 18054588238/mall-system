package com.personal.mall.product.service.impl;

import com.personal.mall.product.constant.ProductConstant;
import com.personal.mall.product.dao.AttrAttrgroupRelationDao;
import com.personal.mall.product.entity.AttrAttrgroupRelationEntity;
import com.personal.mall.product.entity.AttrEntity;
import com.personal.mall.product.entity.vo.AttrGroupWithAttrsVO;
import com.personal.mall.product.entity.vo.AttrRelaDelVO;
import com.personal.mall.product.service.AttrService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.common.utils.PageUtils;
import com.personal.common.utils.Query;

import com.personal.mall.product.dao.AttrGroupDao;
import com.personal.mall.product.entity.AttrGroupEntity;
import com.personal.mall.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationDao relationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        //获取检索的关键字
        String key = (String) params.get("key");
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(key)) {
            // 拼接查询条件
            wrapper.and((obj) -> {
                obj.eq("attr_group_id",key)
                        .or().like("attr_group_name",key);
            });
        }

        if (catelogId == 0) {
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wrapper
            );
            return new PageUtils(page);
        }
        wrapper.eq("catelog_id",catelogId);
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

    @Override
    public List<AttrEntity> getRelaAtrr(Long attrgroupId) {
        List<Long> attrId = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .eq("attr_group_id", attrgroupId))
                .stream()
                .map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
        if (attrId.isEmpty()) {
            return null;
        }
        List<AttrEntity> attrEntities = attrService.list(new QueryWrapper<AttrEntity>().in("attr_id", attrId));
        return attrEntities;
    }

    @Override
    public void deleteRelaAtrr(AttrRelaDelVO[] attrRelaDelVO) {
        ArrayList<AttrAttrgroupRelationEntity> list = new ArrayList<>();
        // 删除关联关系表中的数据
        Arrays.asList(attrRelaDelVO).forEach(item -> {
            List<AttrAttrgroupRelationEntity> relationEntities = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq(
                    "attr_group_id", item.getAttrGroupId()).eq("attr_id", item.getAttrId()));
            list.addAll(relationEntities);
        });
        relationDao.deleteBatchIds(list);
    }

    @Override
    public List<AttrGroupWithAttrsVO> getWithattr(Long catalogId) {
        List<AttrGroupEntity> groupEntity = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catalogId));
        List<AttrGroupWithAttrsVO> withAttrsVOS = new ArrayList<>();
        groupEntity.forEach(item -> {
            AttrGroupWithAttrsVO groupWithAttrsVO = new AttrGroupWithAttrsVO();
            BeanUtils.copyProperties(item, groupWithAttrsVO);
            List<Long> attrIds = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>()
                            .eq("attr_group_id", item.getAttrGroupId())).stream()
                    .map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
            if (!attrIds.isEmpty()) {
                List<AttrEntity> attrEntities = attrService.list(new QueryWrapper<AttrEntity>().in("attr_id", attrIds));
                groupWithAttrsVO.setAttrs(attrEntities);
            }
            withAttrsVOS.add(groupWithAttrsVO);
        });
        return withAttrsVOS;
    }
}