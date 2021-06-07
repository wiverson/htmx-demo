package com.devhow.htmxdemo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/private/top-secret")
public class TopSecret {

    @GetMapping("/")
    public String index()
    {
        return "private-index";
    }

}
