package com.devhow.htmxdemo;

import j2html.tags.ContainerTag;
import j2html.tags.specialized.PTag;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;

import static j2html.TagCreator.*;

@Controller
@RequestMapping("/public/input")
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

    @PostMapping(path = "/button", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String button(@RequestParam("demo-button") String button) {
        return p("Button " + button + " clicked.").render();
    }

    @PostMapping(path = "/checkbox", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String checkbox(@RequestParam Map<String, String> parameters) {

        if (parameters.containsKey("checkbox"))
            if (parameters.containsKey(parameters.get("checkbox")))
                return p("Checkbox " + parameters.get("checkbox") + " checked.").render();

        return p("Checkbox " + parameters.get("checkbox") + " unchecked.").render();
    }

    @PostMapping(path = "/radio", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String radio(@RequestParam("demo-radio") String selection) {
        return p("Radio " + selection + " selected.").render();
    }

    @PostMapping(path = "/slider", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String slider(@RequestParam("demo-range") Integer selection) {
        return p("Slider " + selection + " value.").render();
    }

    @PostMapping(path = "/select-single", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String selectSingle(@RequestParam("demo-select-single") String selection) {
        return p("Selected " + selection + ".").render();
    }

    @PostMapping(path = "/select-multiple", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String selectMultiple(@RequestParam("demo-select-multiple") String[] selection) {
        ContainerTag<PTag> p = p("Selected");

        for (String s : selection)
            p.with(span(" " + s));
        return p.render();
    }

    @PostMapping(path = "/date", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String date(@RequestParam("demo-date") String date) {
        LocalDate parse = LocalDate.parse(date);
        return p("Selected " + parse + ".").render();
    }

    @PostMapping(path = "/time", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String time(@RequestParam("demo-time") String time) {
        LocalTime parse = LocalTime.parse(time);
        return p("Selected " + parse + ".").render();
    }

    @PostMapping(path = "/datetime", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String datetime(@RequestParam("demo-date-time-local") String datetime) {
        LocalDateTime parse = LocalDateTime.parse(datetime);
        return p("Selected " + parse + ".").render();
    }

    @PostMapping(path = "/color", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String color(@RequestParam("demo-color") String color) {

        Color c = Color.decode(color);

        return p(join(
                "Hex:",
                color,
                " RGB:",
                c.getRed() + "",
                c.getGreen() + "",
                c.getBlue() + ""
        )).render();
    }

    @PostMapping(path = "/number", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String color(@RequestParam("demo-number") Integer num) {
        return p("Number: " + num).render();
    }

    @PostMapping(path = "/text", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String text(@RequestHeader("HX-Trigger-Name") String trigger, @RequestParam Map<String, String> parameters) {
        String target = parameters.get(trigger);
        if (!parameters.containsKey(trigger))
            return "";
        return p(trigger + " set to " + target).render();
    }

    @PostMapping(path = "/file", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String file(
            @RequestParam("demo-file") MultipartFile file,
            @RequestParam Map<String, String> parameters) {
        ContainerTag<PTag> p = p("File uploaded! ").with(join(br(),
                " File name: " + file.getName(), br(),
                " File length: " + file.getSize() + " bytes", br(),
                " File type: " + file.getContentType(), br(),
                " Original file name: " + file.getOriginalFilename()
        ));

        return p.render();
    }

    @PostMapping(path = "/reset", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String reset() {
        return p("Form reset!").render();
    }

    @PostMapping(path = "/submit", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String submit(@RequestParam Map<String, String> parameters) {

        var p = p("Form submitted!");

        for (String s : parameters.keySet())
            p.with(join(br(), s + ":" + parameters.get(s)));

        return p.render();
    }


}
