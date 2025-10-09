package com.personal.mall.seckill.schedule;

import com.personal.mall.seckill.service.SkuSeckillService;
import com.personal.mall.seckill.utils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@EnableScheduling
@EnableAsync  // 开启异步同步
@Component
@Slf4j
public class SeckillSkuSchedule {

    @Autowired
    private SkuSeckillService skuSeckillService;
    @Autowired
    private RedissonClient redissonClient;

    // 定时上架秒杀商品
    @Async
    @Scheduled(cron = "*/5 * * * * *")
    public void upSeckillSkuLatestDays(){
        log.info("商品上架----------！");
        // 分布式锁，确保同一时刻只能有一个线程执行代码
        RLock lock = redissonClient.getLock("seckill:upload:lock");
        lock.lock(10, TimeUnit.SECONDS);
        try {
            // 上架近三天的秒杀商品
            // 查询秒杀活动
            skuSeckillService.seckillSkuLatestDays(DateTimeUtil.getLatestDays(3));
        }catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            lock.unlock();
        }

    }
}
