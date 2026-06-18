package com.example.springbootdemobackend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {

    @RequestMapping("/Hello")
    public String test() {
        return "欢迎来到重庆理工大学！";
    }

}