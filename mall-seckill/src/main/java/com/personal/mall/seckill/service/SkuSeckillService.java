package com.personal.mall.seckill.service;

import com.personal.mall.seckill.dto.SeckillSkuRedisDTO;

import java.util.List;

public interface SkuSeckillService {
    // 查询近三天的秒杀活动信息
    void seckillSkuLatestDays(String[] latestDays);

    List<SeckillSkuRedisDTO> getCurSeckillSkus();

    SeckillSkuRedisDTO getSeckillSkuBySkuId(Long skuId);
}
