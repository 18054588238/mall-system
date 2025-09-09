package com.personal.mall.search.controller;

import com.personal.mall.search.service.MallSearchService;
import com.personal.mall.search.vo.SearchParam;
import com.personal.mall.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class SearchController {
    @Autowired
    private MallSearchService mallSearchService;

    @GetMapping(value = {"/list.html","/","/index.html"})
    public String listPage(SearchParam param, Model model) throws IOException {
        SearchResult result = mallSearchService.search(param);
        model.addAttribute("result",result);
        return "index";
    }
}
