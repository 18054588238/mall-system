package com.personal.mall.coupon.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.personal.mall.coupon.entity.SeckillSessionEntity;
import com.personal.mall.coupon.service.SeckillSessionService;
import com.personal.common.utils.PageUtils;
import com.personal.common.utils.R;



/**
 * 秒杀活动场次
 *
 * @author liupanpan
 * @email merdermi902@gmail.com
 * @date 2025-07-29 20:12:13
 */
@RestController
@RequestMapping("coupon/seckillsession")
public class SeckillSessionController {
    @Autowired
    private SeckillSessionService seckillSessionService;

    // 获取近几天的商品秒杀信息
    @RequestMapping("/getSeckillSkuLatestDays")
    public R getSeckillSkuLatestDays(@RequestBody String[] latestDays) {
        List<SeckillSessionEntity> data = seckillSessionService.getSeckillSkuLatestDays(latestDays);
        return R.ok().put("data", JSON.toJSONString(data));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = seckillSessionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		SeckillSessionEntity seckillSession = seckillSessionService.getById(id);

        return R.ok().put("seckillSession", seckillSession);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SeckillSessionEntity seckillSession){
		seckillSessionService.save(seckillSession);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SeckillSessionEntity seckillSession){
		seckillSessionService.updateById(seckillSession);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		seckillSessionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
