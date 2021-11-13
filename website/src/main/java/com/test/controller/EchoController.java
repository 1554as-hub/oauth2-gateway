package com.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EchoController {

    @Autowired
    private Environment env;

    @GetMapping("/echo/{param}")
    public String echo(@PathVariable(value = "param") String param){
        StringBuilder stb = new StringBuilder();
        stb.append("website service").append(param).append(" mark")
                .append(env.getProperty("mark"));
        return stb.toString();
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello wrold";
    }


}
