package com.personal.mall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.personal.common.exception.BizCodeEnum;
import com.personal.common.exception.WareNoStockException;
import com.personal.mall.ware.entity.vo.OrderWareLockVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.personal.mall.ware.entity.WareSkuEntity;
import com.personal.mall.ware.service.WareSkuService;
import com.personal.common.utils.PageUtils;
import com.personal.common.utils.R;



/**
 * 商品库存
 *
 * @author liupanpan
 * @email merdermi902@gmail.com
 * @date 2025-07-29 20:22:11
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;

    // 根据订单锁定库存
    @RequestMapping("/lockWareStock")
    public R lockWareStock(@RequestBody List<OrderWareLockVO> vos) {
        try {
            wareSkuService.lockWareStock(vos);
        } catch (WareNoStockException e) {
            return R.error(BizCodeEnum.NO_STOCK_EXCEPTION.getCode(), BizCodeEnum.NO_STOCK_EXCEPTION.getMsg());
        }
        return R.ok();
    }

    /**
     * 返回有库存的sku_id列表
     */
    @PostMapping("/haveStock")
    public List<Long> haveStock(@RequestBody List<Long> skuIds){

        return wareSkuService.haveStock(skuIds);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
