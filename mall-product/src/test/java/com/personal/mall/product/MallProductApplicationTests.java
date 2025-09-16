package com.personal.mall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.personal.mall.product.entity.BrandEntity;
import com.personal.mall.product.entity.vo.ItemVO;
import com.personal.mall.product.service.BrandService;
import com.personal.mall.product.service.CategoryService;
import com.personal.mall.product.service.SkuInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.Random;

@SpringBootTest
class MallProductApplicationTests {

    @Autowired
    private BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    SkuInfoService skuInfoService;

    @Test
    void contextLoads() {

    }

}
