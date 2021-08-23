package com.devhow.htmxdemo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/public/click-to-edit")
public class ClickToEdit {

    @GetMapping
    public String start(Model model) {
        model.addAttribute("contact", Contact.demoContact());
        model.addAttribute("now", new Date().toInstant());

        return "click-to-edit";
    }

    @PostMapping("/edit/{id}")
    public String editForm(Contact contact, Model model, @PathVariable String id) {
        model.addAttribute("contact", contact);
        model.addAttribute("id", id);
        return "click-to-edit-form";
    }

    @PostMapping("/commit")
    public String editPost(Contact contact, Model model) {
        model.addAttribute("contact", contact);
        return "click-to-edit-default";
    }

}
