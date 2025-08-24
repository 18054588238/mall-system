package com.personal.mall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.personal.mall.product.entity.BrandEntity;
import com.personal.mall.product.service.BrandService;
import com.personal.mall.product.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MallProductApplicationTests {

    @Autowired
    private BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Test
    void contextLoads() {
//        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1));
//        for (BrandEntity entity : list) {
//            System.out.println(entity);
//        }
        Long[] path = categoryService.getCatelogPath(476l);
        for (int i = 0; i < path.length; i++) {
            System.out.println(path[i]);
        }
    }

}
