package com.devhow.htmxdemo.demo;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

import static org.springframework.web.util.HtmlUtils.htmlEscape;

@Controller
@RequestMapping("/infinite-scroll")
public class InfiniteScroll {

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

    // language=html
    String loadText = """
            <p><b>hello</b></p>
            """;

    @PostMapping(value =
            "/page/{id}", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String editForm(Model model) {
        Contact contact = new Contact();

        contact.firstName = "first";
        contact.lastName = "last";
        contact.email = "first.last@example.com";

        model.addAttribute("contact", contact);

        return htmlEscape(loadText.formatted());
    }

    @PostMapping("/commit")
    public String editPost(Contact contact, Model model) {
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
