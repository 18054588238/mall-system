package com.personal.mall.product.web;

import com.personal.mall.product.entity.CategoryEntity;
import com.personal.mall.product.entity.vo.Catalog2VO;
import com.personal.mall.product.service.CategoryService;
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

}
