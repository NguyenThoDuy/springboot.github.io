package com.spring.upload_file.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class MainController {
    @GetMapping("")
    public String main(){
        return "main-layout";
    }

    @GetMapping("main")
    public String show(){
        return "demo";
    }

}
