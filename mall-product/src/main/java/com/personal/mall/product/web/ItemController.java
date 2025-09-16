package com.personal.mall.product.web;

import com.personal.mall.product.entity.vo.ItemVO;
import com.personal.mall.product.service.SkuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ItemController {
//    "http://mall.item.com/"+skuId+".html"
    @Autowired
    private SkuInfoService skuInfoService;

    @GetMapping("/{skuId}.html")
    public String item(@PathVariable("skuId") Long skuId, Model model) {
        ItemVO item = skuInfoService.item(skuId);
        model.addAttribute("item", item);
        return "item";
    }
}
