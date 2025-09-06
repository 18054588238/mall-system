package com.personal.mall.product.web;

import com.personal.mall.product.entity.CategoryEntity;
import com.personal.mall.product.entity.vo.Catalog2VO;
import com.personal.mall.product.service.CategoryService;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IndexController
 * @Author liupanpan
 * @Date 2025/8/29
 * @Description
 */
@Controller
public class IndexController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedissonClient redissonClient;

    // 加载商城首页时需要获取分类信息
    @GetMapping({"/","/index","index.html","/home","/home.html"})
    public String index(Model model) {
        List<CategoryEntity> list = categoryService.getLevel1Category();
        // 将一级分类信息封装到model中，传递给前端
        model.addAttribute("categorys",list);
        return "index";
    }

    @ResponseBody // 将方法的返回结果直接写入http响应正文中，并根据数据类型自动转换为JSON或字符串格式，避免视图跳转
    @RequestMapping("/index/catalog.json")
    public Map<String,List<Catalog2VO>> getCatalog2JSON() {
        return categoryService.getCatalog2JSON();
    }

    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        /*可重入锁*/
        RLock myLock = redissonClient.getLock("myLock");
        /*写锁*/
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("myReadWriteLock");
        RLock rLock = readWriteLock.writeLock();
        /*闭锁*/
        RCountDownLatch countDownLatch = redissonClient.getCountDownLatch("myCountDownLatch");
        countDownLatch.trySetCount(5);
        try {
            countDownLatch.await(); // 等待数量降到0
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        countDownLatch.countDown();// 递减
        /*信号量*/
        RSemaphore semaphore = redissonClient.getSemaphore("mySemaphore");
//        semaphore.acquire();
        semaphore.release();

        rLock.lock();
        myLock.lock(); // 加锁

        try {
            System.out.println("业务处理。。。"+Thread.currentThread().getName());
        } catch (Exception e) {

        } finally {
            myLock.unlock(); // 释放锁
            rLock.unlock();
        }

        return "hello";
    }

}
