package com.demo.jwtspringboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {


    @RequestMapping("/login")
    public String login() {
        return "login"; // 返回登录页面的视图名称
    }
    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("message", "Halo dari controller");
        return "index"; // artinya -> render file templates/index.ftl
    }

}
