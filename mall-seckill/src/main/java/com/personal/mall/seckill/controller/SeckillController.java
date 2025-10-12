package com.personal.mall.seckill.controller;

import com.alibaba.fastjson.JSON;
import com.personal.common.utils.R;
import com.personal.mall.seckill.dto.SeckillSkuRedisDTO;
import com.personal.mall.seckill.service.SkuSeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SeckillController
 * @Author liupanpan
 * @Date 2025/10/9
 * @Description
 */
@RestController
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private SkuSeckillService skuSeckillService;

    // 查询当前秒杀商品
    @GetMapping("/getCurSeckillSkus")
    public R getCurSeckillSkus() {
        List<SeckillSkuRedisDTO> data = skuSeckillService.getCurSeckillSkus();
        return R.ok().put("data", JSON.toJSONString(data));
    }

    // 根据skuid查询秒杀商品
    @GetMapping("/getSeckillSkuBySkuId")
    public R getSeckillSkuBySkuId(@RequestParam("skuId") Long skuId) {
        SeckillSkuRedisDTO data = skuSeckillService.getSeckillSkuBySkuId(skuId);
        return R.ok().put("data", JSON.toJSONString(data));
    }

    // 进行商品秒杀
    @GetMapping("/onSeckill")
    public R onSeckill(@RequestParam("key") String key,@RequestParam("randomCode") String randomCode,@RequestParam("num") Integer num) {
        String s = skuSeckillService.onSeckill();
        return R.ok().put("data","test");
    }
}
