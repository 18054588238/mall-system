package com.personal.mall.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.personal.mall.product.entity.vo.AttrGroupEntityVO;
import com.personal.mall.product.entity.vo.AttrResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.personal.mall.product.entity.AttrEntity;
import com.personal.mall.product.service.AttrService;
import com.personal.common.utils.PageUtils;
import com.personal.common.utils.R;



/**
 * 商品属性
 *
 * @author liupanpan
 * @email merdermi902@gmail.com
 * @date 2025-07-28 19:20:32
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @GetMapping("/{attrType}/list/{catelogId}")
    public R baseList(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId, @PathVariable("attrType") String attrType) {
        PageUtils page = attrService.queryBasePage(params,catelogId,attrType);
        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
//		AttrEntity attr = attrService.getById(attrId);
		AttrResponseVO attr = attrService.getAttrInfo(attrId);

        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntityVO attr){
//		attrService.save(attr);
        attrService.saveAndGroup(attr);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntityVO attr){
//		attrService.updateById(attr);
		attrService.updateBaseAttr(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
//		attrService.removeByIds(Arrays.asList(attrIds));
        // 删除对应分组关联表信息
		attrService.removeByIdsDetails(Arrays.asList(attrIds));

        return R.ok();
    }

}
