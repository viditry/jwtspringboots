package com.demo.jwtspringboot.rest;

import com.alibaba.fastjson2.JSON;
import com.demo.jwtspringboot.service.WmsCDocService;
import com.demo.jwtspringboot.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class HelloController {
    @Autowired
    private WmsCDocService wmsCDocService;


    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }


    @GetMapping("/wmsCDoc/{werks}/{doctype}")
    public R wmsCDoc(@PathVariable String werks, @PathVariable String docType) {
        Map<String, Object> map = wmsCDocService.getDocNo(werks, docType);
        String test = JSON.toJSONString(map);
        return R.ok().put("list", test);
    }
}