package com.devhow.htmxdemo.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/click-to-edit")
public class ClickToEdit {

    @GetMapping
    public String start(Model model) {
        Contact contact = new Contact();
        contact.firstName = "Bob";
        contact.lastName = "Smith";
        contact.email = "bsmith@example.com";
        model.addAttribute("contact", contact);

        model.addAttribute("now", new Date().toInstant().toString());

        return "click-to-edit";
    }

    @PostMapping("/edit/{id}")
    public String editForm(Contact contact, Model model) {
        model.addAttribute("contact", contact);
        return "click-to-edit-form";
    }

    @PostMapping("/commit/{id}")
    public String editPost(Contact contact, Model model) {
        model.addAttribute("contact", contact);
        return "click-to-edit-default";
    }

    @PostMapping("/cancel/{id}")
    public String cancelEdit(Contact contact, Model model) {
        model.addAttribute("contact", contact);
        return "click-to-edit-default";
    }

    public class Contact {
        private String firstName;
        private String lastName;
        private String email;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

}
