package com.personal.mall.search.controller;

import com.personal.common.dto.SkuESModel;
import com.personal.common.exception.BizCodeEnum;
import com.personal.common.utils.R;
import com.personal.mall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RequestMapping("search/save")
@RestController
@Slf4j
public class ElasticSearchSaveController {
    @Autowired
    private ProductSaveService productSaveService;

    // 将上架信息保存到es中
    @PostMapping("/product")
    public R saveProduct(@RequestBody List<SkuESModel> esModels) throws IOException {
        Boolean b = false;
        try {
            b = productSaveService.saveProduct(esModels);
        } catch (Exception e) {
            log.error("商品上架信息存储到es错误：{}",e.getMessage());
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }
        if (b) {
            return R.ok();
        }
        return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
    }
}
