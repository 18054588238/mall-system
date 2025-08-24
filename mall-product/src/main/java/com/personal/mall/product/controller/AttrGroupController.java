package com.personal.mall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.personal.mall.product.entity.AttrAttrgroupRelationEntity;
import com.personal.mall.product.entity.AttrEntity;
import com.personal.mall.product.entity.vo.AttrGroupWithAttrsVO;
import com.personal.mall.product.entity.vo.AttrRelaDelVO;
import com.personal.mall.product.service.AttrAttrgroupRelationService;
import com.personal.mall.product.service.AttrService;
import com.personal.mall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.personal.mall.product.entity.AttrGroupEntity;
import com.personal.mall.product.service.AttrGroupService;
import com.personal.common.utils.PageUtils;
import com.personal.common.utils.R;



/**
 * 属性分组
 *
 * @author liupanpan
 * @email merdermi902@gmail.com
 * @date 2025-07-28 19:20:32
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private AttrAttrgroupRelationService relationService;


    @GetMapping("/{catelogId}/withattr")
    public R getWithattr(@PathVariable("catelogId") Long catelogId){
        // 根据类别查分组，根据分组查关联表，获取属性信息
        List<AttrGroupWithAttrsVO> data = attrGroupService.getWithattr(catelogId);
        return R.ok().put("data", data);
    }

    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody AttrRelaDelVO[] attrRelaDelVO) {
        relationService.addRelaAttr(attrRelaDelVO);
        return R.ok();
    }

    // 获取未被关联的属性信息
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R noattrRelation(@PathVariable("attrgroupId") Long attrgroupId,@RequestParam Map<String,Object> params){
        PageUtils pageUtils = attrService.getNoRelaAtrr(attrgroupId,params);
        return R.ok().put("page", pageUtils);
    }

    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrRelaDelVO[] attrRelaDelVO) {
        attrGroupService.deleteRelaAtrr(attrRelaDelVO);
        return R.ok();
    }

    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId){
        List<AttrEntity> data = attrGroupService.getRelaAtrr(attrgroupId);
        return R.ok().put("data", data);
    }
    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    public R list(@RequestParam Map<String, Object> params,@PathVariable Long catelogId){
//        PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPage(params,catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long[] catelogPath = categoryService.getCatelogPath(attrGroup.getCatelogId());
        attrGroup.setCatelogPath(catelogPath);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
