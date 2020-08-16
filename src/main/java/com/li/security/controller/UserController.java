package com.li.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }

   /* @PostMapping("/login")
    public String index(String username, String password) {
        return "index";
    }*/

    @GetMapping("/syslog")
    public String syslog() {
        return "syslog";
    }

    @GetMapping("/sysuser")
    public String sysuser() {
        return "sysuser";
    }

    @GetMapping("/biz1")
    public String biz1() {
        return "biz1";
    }

    @GetMapping("/biz2")
    public String biz2() {
        return "biz2";
    }

}
