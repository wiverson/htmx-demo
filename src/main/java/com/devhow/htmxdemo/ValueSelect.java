package com.devhow.htmxdemo;

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
@RequestMapping("/public/value-select")
public class ValueSelect {

    /**
     * IntelliJ has a plugin that supports handlebars inline (and file template) syntax highlighting.
     * <p>
     * https://plugins.jetbrains.com/plugin/6884-handlebars-mustache
     */
    @Language("handlebars")
    private final String handleBarTemplate =
            """
                    {{#each}}
                    <option value="{{this}}">{{{this}}}</option>
                    {{/each}}
                    """;
    private final String[] java8 = {"lambdas", "collections", "streams"};
    private final String[] java9 = {"collections", "streams", "optionals", "interfaces", "jshell"};
    private final String[] java10 = {"var"};
    private final String[] java11 = {"strings", "scripts", "lambda var"};
    private final String[] java12 = {"unicode 11"};
    private final String[] java13 = {"unicode 12"};
    private final String[] java14 = {"switch", "better null pointer error messages"};
    private final String[] java15 = {"text blocks", "Z garbage collector"};
    private final String[] java16 = {"sockets", "records"};
    private final String[] java17 = {"pattern matching for switch","sealed classes", "foreign function and memory api"};
    private final String[] java18 = {"UTF-8 by default", "jwebserver"};
    private final String[] java19 = {"virtual threads", "structured concurrency", "vector api"};
    private final String[] java20 = {"scoped values", "record patterns"};
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
        if ("java8".equals(make))
            return template.apply(java8);
        if ("java9".equals(make))
            return template.apply(java9);
        if ("java10".equals(make))
            return template.apply(java10);
        if ("java11".equals(make))
            return template.apply(java11);
        if ("java12".equals(make))
            return template.apply(java12);
        if ("java13".equals(make))
            return template.apply(java13);
        if ("java14".equals(make))
            return template.apply(java14);
        if ("java15".equals(make))
            return template.apply(java15);
        if ("java16".equals(make))
            return template.apply(java16);
        if ("java17".equals(make))
            return template.apply(java17);
        if ("java18".equals(make))
            return template.apply(java18);
        if ("java19".equals(make))
            return template.apply(java19);
        if ("java20".equals(make))
            return template.apply(java20);
        throw new IllegalArgumentException("Unknown make");
    }
}
