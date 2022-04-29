package com.jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j // lombok을 사용하면 LoggerFactory를 자동으로 생성해줌
public class HomeController {


    @RequestMapping("/")
    public String home(){
        return "home";
    }
}
