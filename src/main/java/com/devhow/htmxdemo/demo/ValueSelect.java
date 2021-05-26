package com.devhow.htmxdemo.demo;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.intellij.lang.annotations.Language;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping("/value-select")
public class ValueSelect {


    /**
     * IntelliJ has a plugin that supports handlebars inline (and file template) syntax highlighting.
     * <p>
     * https://plugins.jetbrains.com/plugin/6884-handlebars-mustache
     */
    @Language("handlebars")
    String handleBarTemplate =
            """
                    <option value="a1">{{{this}}}</option>
                    """;

    Template template;

    public ValueSelect() {
        Handlebars handlebars = new Handlebars();
        try {
            template = handlebars.compileInline(handleBarTemplate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping
    public String start(Model model) {
        model.addAttribute("now", new Date().toInstant());
        return "value-select";
    }

    @GetMapping(value = "/models", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String models(@RequestParam("make") String make) throws IOException {
        return template.apply( make + " models!");
    }
}
