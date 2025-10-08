package com.personal.mall.seckill.service;

public interface SkuSeckillService {
    // 查询近三天的秒杀活动信息
    void seckillSkuLatestDays(String[] latestDays);

}
