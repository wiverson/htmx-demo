package com.devhow.htmxdemo;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/private/")
public class TopSecret {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("now", new Date());
        return "private-index";
    }

    @GetMapping(path = "/data", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String data() {
        return "<p>hi! %s </p>".formatted(new Date().toString());
    }

}
