package com.personal.mall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.personal.mall.ware.entity.vo.MergeVO;
import com.personal.mall.ware.entity.vo.PurchaseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.personal.mall.ware.entity.PurchaseEntity;
import com.personal.mall.ware.service.PurchaseService;
import com.personal.common.utils.PageUtils;
import com.personal.common.utils.R;



/**
 * 采购信息
 *
 * @author liupanpan
 * @email merdermi902@gmail.com
 * @date 2025-07-29 20:22:11
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    // 领取采购单
    @PostMapping("/done")
    public R done(@RequestBody PurchaseVO vo) {
        purchaseService.done(vo);

        return R.ok();
    }

    // 领取采购单
    @PostMapping("/receive")
    public R receive(@RequestBody List<Long> purchaseIds) {
        purchaseService.receive(purchaseIds);

        return R.ok();
    }

    @PostMapping("/merge")
    public R merge(@RequestBody MergeVO vo) {
        Integer flag = purchaseService.merge(vo);

        return R.ok();
    }

    @RequestMapping("/unreceive/list")
    public R unreceiveList(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPageUnreceive(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PurchaseEntity purchase){
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
