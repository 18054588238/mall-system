package com.personal.mall.product.service.impl;

import com.personal.mall.product.service.CategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.common.utils.PageUtils;
import com.personal.common.utils.Query;

import com.personal.mall.product.dao.CategoryDao;
import com.personal.mall.product.entity.CategoryEntity;
import com.personal.mall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 查询所有的类别数据，然后将数据封装为树形结构，便于前端使用
     *
     * @param params
     * @return
     */
    @Override
    public List<CategoryEntity> queryPageWithTree(Map<String, Object> params) {
        // 所有类别数据
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        List<CategoryEntity> collect = categoryEntities.stream()
                .filter(cate -> cate.getParentCid() == 0)  // 一级分类
                .peek(cate -> {
                    // 根据大类找到所有的小类
                    cate.setChildren(getCategoryChildren(cate, categoryEntities));
                })
                .sorted(Comparator.comparingInt(
                        entity -> entity.getSort() != null ? entity.getSort() : 0
                )).collect(Collectors.toList());
        return collect;
    }

    @Override
    public Long[] getCatelogPath(Long attrGroupId) {
        List<Long> paths = new ArrayList<>();
        getPath(attrGroupId,paths);
        Collections.reverse(paths);
        return paths.toArray(new Long[paths.size()]);
    }

    @Transactional
    @Override
    public void updateDetail(CategoryEntity category) {
        this.updateById(category);
        if (!StringUtils.isEmpty(category.getName())) {
            categoryBrandRelationService.updateCategoryName(category.getCatId(),category.getName());
        }
    }

    private void getPath(Long attrGroupId, List<Long> paths) {
        paths.add(attrGroupId);
        Long cid = this.getById(attrGroupId).getParentCid();
        if ( cid != 0) {
            getPath(cid,paths);
        }
    }

    private List<CategoryEntity> getCategoryChildren(CategoryEntity cate, List<CategoryEntity> categoryEntities) {
        // 二级分类
        return categoryEntities.stream()
                .filter(category -> Objects.equals(category.getParentCid(), cate.getCatId())) // 二级分类
                .peek(category -> {
                    // 为当前category设置子节点
                    category.setChildren(getCategoryChildren(category, categoryEntities));
                })
                // 对子分类列表按 sort 字段升序排序
                .sorted(Comparator.comparingInt(
                        entity -> entity.getSort() != null ? entity.getSort() : 0
                )).collect(Collectors.toList());
    }

}