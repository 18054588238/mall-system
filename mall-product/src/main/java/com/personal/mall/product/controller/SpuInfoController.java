package com.personal.mall.product.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.personal.mall.product.entity.vo.OrderItemSpuInfoVO;
import com.personal.mall.product.entity.vo.SpuInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.personal.mall.product.entity.SpuInfoEntity;
import com.personal.mall.product.service.SpuInfoService;
import com.personal.common.utils.PageUtils;
import com.personal.common.utils.R;



/**
 * spu信息
 *
 * @author liupanpan
 * @email merdermi902@gmail.com
 * @date 2025-07-28 19:20:32
 */
@RestController
@RequestMapping("product/spuinfo")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;

    @RequestMapping("/getOrderItem/{spuIds}")
    public List<OrderItemSpuInfoVO> getOrderItemSpuInfoBySpuId(@PathVariable("spuIds") Set<Long> spuIds){
        return spuInfoService.getOrderItemSpuInfoBySpuId(spuIds);
    }

    /**
     * 商品上架
     */
    @PostMapping("/{spuId}/up")
    public R up(@PathVariable("spuId") Long spuId){
        try {
            spuInfoService.up(spuId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
//        PageUtils page = spuInfoService.queryPage(params);
        PageUtils page = spuInfoService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SpuInfoVO vo){
//		spuInfoService.save(spuInfo);
		spuInfoService.saveSpuInfo(vo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
