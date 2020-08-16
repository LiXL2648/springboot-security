package com.li.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        return "hello world";
    }

    @PostMapping("/index")
    public String index() {
        return "index";
    }
}
