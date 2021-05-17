package com.devhow.htmxdemo.demo;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static j2html.TagCreator.*;

@Controller
@RequestMapping("/input")
public class InputCatalog {

    @GetMapping
    public String start(Model model) {
        model.addAttribute("now", new Date().toInstant());
        return "input-catalog";
    }

    @DeleteMapping(path = "/delete", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String delete() {
        return "";
    }

    @PostMapping(path = "/create", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String create(@RequestParam("new-todo") String todo) {

        if (todo == null || todo.length() < 1)
            return "";

        return tr(
                td(todo).attr("scope", "row"),
                td(input().withType("checkbox")),
                td(input().withType("button").withClasses("btn", "btn-danger").withValue("Delete")
                        .attr("hx-confirm", "Are you sure?")
                        .attr("hx-target", "closest tr")
                        .attr("hx-swap", "outerHTML swap:1s")
                        .attr("hx-delete", "/todo/delete")
                        .attr("hx-trigger", "click")
                        .withClasses("btn", "btn-danger")
                )
        ).renderFormatted();
    }
}
