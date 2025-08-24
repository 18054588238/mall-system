package com.personal.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.personal.mall.product.constant.ProductConstant;
import com.personal.mall.product.dao.AttrAttrgroupRelationDao;
import com.personal.mall.product.entity.AttrAttrgroupRelationEntity;
import com.personal.mall.product.entity.AttrGroupEntity;
import com.personal.mall.product.entity.CategoryEntity;
import com.personal.mall.product.entity.vo.AttrGroupEntityVO;
import com.personal.mall.product.entity.vo.AttrResponseVO;
import com.personal.mall.product.service.AttrAttrgroupRelationService;
import com.personal.mall.product.service.AttrGroupService;
import com.personal.mall.product.service.CategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.common.utils.PageUtils;
import com.personal.common.utils.Query;

import com.personal.mall.product.dao.AttrDao;
import com.personal.mall.product.entity.AttrEntity;
import com.personal.mall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    AttrAttrgroupRelationService relationService;

    @Autowired
    CategoryService categoryService;
    @Autowired
    AttrGroupService attrGroupService;
    @Autowired
    AttrAttrgroupRelationDao groupRelationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveAndGroup(AttrGroupEntityVO attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        this.save(attrEntity);
        if (attr.getAttrGroupId() != null && attrEntity.getAttrType() == ProductConstant.AttrType.BASE_ATTR_TYPE.getCode()) {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attrEntity.getAttrId());
            relationService.save(relationEntity);
        }
    }

    @Override
    public PageUtils queryBasePage(Map<String, Object> params, Long catelogId, String attrType) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_type", "base".equalsIgnoreCase(attrType)? 1:0);
        // 拼接查询条件
        if (catelogId != 0) {
            wrapper.eq("catelog_id", catelogId);
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.eq("attr_id",key).or().like("attr_name",key);
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );
        List<AttrResponseVO> voList = new ArrayList<>();
        // 给分组名称/分类名称赋值
        // 销售属性不需要给分组信息赋值
        page.getRecords().forEach(attr -> {
            AttrResponseVO vo = new AttrResponseVO();
            BeanUtils.copyProperties(attr,vo);
            CategoryEntity categoryEntity = categoryService.getById(attr.getCatelogId());
            if (categoryEntity != null) {
                vo.setCatelogName(categoryEntity.getName());
            }
            if ("base".equalsIgnoreCase(attrType)) {
                AttrAttrgroupRelationEntity relationEntity = groupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>()
                        .eq("attr_id", attr.getAttrId()));
                if (relationEntity != null && relationEntity.getAttrGroupId() != null) {
                    AttrGroupEntity groupEntity = attrGroupService.getById(relationEntity.getAttrGroupId());
                    vo.setGroupName(groupEntity.getAttrGroupName());
                }
            }
            voList.add(vo);
        });
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(voList);
        return pageUtils;
    }

    @Override
    public AttrResponseVO getAttrInfo(Long attrId) {
        AttrResponseVO vo = new AttrResponseVO();
        AttrEntity entity = this.getById(attrId);
        BeanUtils.copyProperties(entity,vo);

        // 给所属分组、分类赋值
        if (entity.getAttrType() == ProductConstant.AttrType.BASE_ATTR_TYPE.getCode()) {
            AttrAttrgroupRelationEntity relationEntity = groupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
            if (relationEntity != null && relationEntity.getAttrGroupId() != null) {
                AttrGroupEntity groupEntity = attrGroupService.getById(relationEntity.getAttrGroupId());
                vo.setAttrGroupId(groupEntity.getAttrGroupId());
                vo.setGroupName(groupEntity.getAttrGroupName());
            }
        }

        CategoryEntity categoryEntity = categoryService.getById(entity.getCatelogId());
        if (categoryEntity != null) {
            vo.setCatelogName(categoryEntity.getName());
        }
        Long[] catelogPath = categoryService.getCatelogPath(entity.getCatelogId());
        vo.setCatelogPath(catelogPath);

        return vo;
    }

    @Transactional
    @Override
    public void updateBaseAttr(AttrGroupEntityVO attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        this.updateById(attrEntity);

        if (attrEntity.getAttrType() == ProductConstant.AttrType.BASE_ATTR_TYPE.getCode()) {
            Long attrId = attr.getAttrId();
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrId(attrId);
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            QueryWrapper<AttrAttrgroupRelationEntity> wrapper = new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq("attr_id", attrId);
            Long count = groupRelationDao.selectCount(wrapper);


            if (count > 0) {
                // 更新
                groupRelationDao.update(relationEntity,wrapper);
            } else {
                // 新增
                groupRelationDao.insert(relationEntity);
            }
        }
    }

    @Transactional
    @Override
    public void removeByIdsDetails(List<Long> ids) {
        for (Long attrId : ids) {
            AttrEntity attrEntity = getById(attrId);
            if (attrEntity.getAttrType() == ProductConstant.AttrType.BASE_ATTR_TYPE.getCode()) {
                groupRelationDao.delete(new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
            }
        }
        this.removeByIds(ids);
    }

    @Override
    public PageUtils getNoRelaAtrr(Long attrgroupId, Map<String, Object> params) {
        // 获取未被关联的属性信息
        // 1.查找该分组关联的类别
        Long catelogId = attrGroupService.getById(attrgroupId).getCatelogId();
        // 属性组编号信息
        List<Long> groupIds = attrGroupService.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId))
                .stream().map(AttrGroupEntity::getAttrGroupId).collect(Collectors.toList());
        // 3.根据分组id，获取关联表中已经存在的属性id ---- 关联其他分组没有关联的属性
        List<Long> attrIds = groupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>()
                        .in("attr_group_id", groupIds))
                .stream()
                .map(AttrAttrgroupRelationEntity::getAttrId)
                .collect(Collectors.toList());
        // 2.根据类别查找所有的属性
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>()
                .eq("catelog_id", catelogId)
                .eq("attr_type", ProductConstant.AttrType.BASE_ATTR_TYPE.getCode());
        if (!attrIds.isEmpty()) {
            wrapper.notIn("attr_id", attrIds);
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and(i->{
                i.eq("attr_id",key).or().like("attr_name",key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>()
                .getPage(params), wrapper);
        return new PageUtils(page);
    }


}