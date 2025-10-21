package com.personal.mall.product.web;

import com.personal.mall.product.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestSentinelController {

    @Autowired
    private TestService testService;

    @GetMapping("/hello/{name}")
    public String apiHello(@PathVariable String name) {
        return testService.sayHello(name);
    }

}
