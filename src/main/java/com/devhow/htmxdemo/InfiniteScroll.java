package com.devhow.htmxdemo;

import org.intellij.lang.annotations.Language;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * This demonstration uses HTML generated here (in the controller!) instead of just the HTML coming from Thymeleaf
 * templates.
 * <p>
 * This is really intended to be a very primitive transitional demonstration, showing the basics of how HTMX could
 * serve as the starting point for a more component-oriented approach, or perhaps even used in combination with
 * WebSockets and Server-Side Events.
 * https://htmx.org/docs/#websockets-and-sse
 * <p>
 * Put another way - this is a pretty messy, hacky mess... but it's also the kernel for starting what could be a
 * different approach.
 */
@Controller
@RequestMapping("/public/infinite-scroll")
public class InfiniteScroll {

    @GetMapping
    public String start(Model model) {
        model.addAttribute("now", new Date().toInstant());
        return "infinite-scroll";
    }

    @Language("html")
    final String contactHtml = """
             <tr>
                 <td>%s</td>
                 <td>%s</td>
                 <td>%s</td>
             </tr>
            """;

    @Language("html")
    final String loadHtml = """
             <tr hx-get="/public/infinite-scroll/page/%d"
                 hx-trigger="revealed"
                 hx-swap="afterend">
                 <td>%s</td>
                 <td>%s</td>
                 <td>%s</td>
             </tr>
            """;

    @GetMapping(value = "/page/{id}", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String nextPage(@PathVariable Integer id) {

        StringBuilder result = new StringBuilder();

        List<Contact> demoContacts = Contact.randomContacts(9);
        for (Contact c : demoContacts) {
            result.append(contactHtml.formatted(c.getFirstName(), c.getLastName(), c.getEmail()));
        }

        Contact last = Contact.randomContacts(1).get(0);

        result.append(loadHtml.formatted(id + 1, last.getFirstName(), last.getLastName(), last.getEmail()));

        // This is just to simulate a slow[er] server response, causing the HTMX wait indicator to display
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}
