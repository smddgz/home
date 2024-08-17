package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Controller
public class IndexController {
    @ResponseBody
    @GetMapping("/time")
    public LocalDateTime time(){
        return LocalDateTime.now();
    }

    @GetMapping
    public String index(){
        return "index";
    }
}



