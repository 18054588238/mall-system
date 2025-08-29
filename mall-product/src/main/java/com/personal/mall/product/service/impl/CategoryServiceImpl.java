package com.personal.mall.product.service.impl;

import com.personal.mall.product.entity.vo.Catalog2VO;
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
        return categoryEntities.stream()
                .filter(cate -> cate.getParentCid() == 0)  // 一级分类
                .peek(cate -> {
                    // 根据大类找到所有的小类
                    cate.setChildren(getCategoryChildren(cate, categoryEntities));
                })
                .sorted(Comparator.comparingInt(
                        entity -> entity.getSort() != null ? entity.getSort() : 0
                )).collect(Collectors.toList());
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

    @Override
    public List<CategoryEntity> getLevel1Category() {
        // 获取以及分类信息
        return this.list(new QueryWrapper<CategoryEntity>()
                .eq("parent_cid",1));
    }

    @Override
    public Map<String, List<Catalog2VO>> getCatalog2JSON() {
        HashMap<String, List<Catalog2VO>> map = new HashMap<>();
        // 一级分类id
        List<Long> level1Ids = this.getLevel1Category().stream()
                .map(CategoryEntity::getCatId).collect(Collectors.toList());
        // 一级分类id和二级分类信息的映射
        Map<Long, List<CategoryEntity>> level2InfosMap = getLevelInfos(2L);

        // 二级分类id和三级分类信息的映射
        Map<Long, List<CategoryEntity>> level3InfosMap = getLevelInfos(3L);

        level1Ids.forEach(l1 -> {
            // 查询一级分类下的子类信息
            List<CategoryEntity> level2Lists = level2InfosMap.get(l1);

            List<Catalog2VO> voList = null;

            if (level2Lists != null) {
                voList = level2Lists.stream()
                        .map(l2 -> {
                            Catalog2VO catalog2VO = new Catalog2VO();

                            catalog2VO.setCatalog1Id(l1.toString());
                            catalog2VO.setId(l2.getCatId().toString());
                            catalog2VO.setName(l2.getName());

                            List<CategoryEntity> level3Lists = level3InfosMap.get(l2.getCatId());
                            List<Catalog2VO.Catalog3VO> catalog3VOS = null;

                            if (level3Lists != null) {
                                catalog3VOS = level3Lists.stream()
                                        .map(l3 -> {
                                            Catalog2VO.Catalog3VO catalog3VO = new Catalog2VO.Catalog3VO();
                                            catalog3VO.setCatalog2Id(l2.getCatId().toString());
                                            catalog3VO.setId(l3.getCatId().toString());
                                            catalog3VO.setName(l3.getName());
                                            return catalog3VO;
                                        }).collect(Collectors.toList());
                            }
                            catalog2VO.setCatalog3VOList(catalog3VOS);

                            return catalog2VO;
                        }).collect(Collectors.toList());
            }
            map.put(l1.toString(),voList);
        });
        return map;
    }

    // 获取父级分类id和子类信息的映射
    public Map<Long, List<CategoryEntity>> getLevelInfos(Long levelId) {

        return this.list(new QueryWrapper<CategoryEntity>()
                        .eq("cat_level", levelId)).stream()
                .collect(Collectors.groupingBy(CategoryEntity::getParentCid));
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