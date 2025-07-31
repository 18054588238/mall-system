package com.personal.mall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.personal.mall.product.entity.BrandEntity;
import com.personal.mall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MallProductApplicationTests {

    @Autowired
    private BrandService brandService;

    @Test
    void contextLoads() {
        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1));
        for (BrandEntity entity : list) {
            System.out.println(entity);
        }
    }

}
