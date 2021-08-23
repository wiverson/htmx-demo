package com.devhow.htmxdemo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

@Controller
public class DemoOverview {

    @GetMapping("/")
    public String overview(Model model) {
        model.addAttribute("now", new Date());
        return "index";
    }

}
