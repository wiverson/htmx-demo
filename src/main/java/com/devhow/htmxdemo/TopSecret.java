package com.devhow.htmxdemo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/private/")
public class TopSecret {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("now", new Date());
        return "private-index";
    }

}
