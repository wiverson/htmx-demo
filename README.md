# htmx-demo

Very simple demonstration of the use of [htmx](https://htmx.org)
with [Spring Boot](https://spring.io/projects/spring-boot)
and [Thymeleaf](https://www.thymeleaf.org). In addition to Thymeleaf, a few examples also use
[Handlebars/Mustache](https://github.com/jknack/handlebars.java) and [j2html](https://j2html.com/).

Build a [Single Page Application (SPA)](https://en.wikipedia.org/wiki/Single-page_application) or just progressively add
dynamic JavaScript functionality to your app without a complicated JavaScript framework.

It's possible to be a "full stack" Java developer and provide rich application functionality without complex tools and
frameworks.

## Using This Project

Requirements: [JDK](https://adoptopenjdk.net/?variant=openjdk16) 16 and [Apache Maven](https://maven.apache.org/).

That's it. No npm, no JavaScript frameworks, no REST services generating JSON that is then converted back to HTML in the
browser.

Once those are installed, just run `mvn spring-boot:run` and hit [http://localhost:8080](http://localhost:8080)
and you'll see an index page with links to the various demos.

You'll notice that you don't need to install Node.js, npm, or any other tooling to get rich, dynamic UIs - just html,
css, htmx, bootstrap and a bit of hyperscript.

If you want to be fancy, tell folks that you
are [migrating back to server-side rendering for your SPA applications](https://blog.asayer.io/server-side-rendering-ssr-with-react)
for performance and security reasons.

## Dependencies

[WebJars](https://www.webjars.org) are used to include Bootstrap, jQuery, htmx and hyperscript. While Bootstrap v5
doesn't technically need jQuery any more, I left it in for now just in case. More information
on [using WebJars with Spring Boot](https://www.webjars.org/documentation#springboot).

You can add the WebJars for [htmx](https://htmx.org/) and [hyperscript](https://hyperscript.org/ to your pom.xml like
this:

```xml

<dependencies>
    <dependency>
        <groupId>org.webjars.npm</groupId>
        <artifactId>htmx.org</artifactId>
        <version>1.4.0</version>
    </dependency>
    <dependency>
        <groupId>org.webjars.npm</groupId>
        <artifactId>hyperscript.org</artifactId>
        <version>0.0.9</version>
    </dependency>
</dependencies>
```

...and then add the following to your application Thymeleaf/HTML to use htmx & hyperscript:

```xml

<head>
    <script type="text/javascript" th:src="@{/webjars/htmx.org/dist/htmx.min.js}"></script>
    <script type="text/javascript" th:src="@{/webjars/hyperscript.org/dist/_hyperscript.js}"></script>
</head>
```

Notice that you don't actually specify the version number for htmx or hyperscript in the HTML declaration - that's all
handled for you by WebJars and your Maven pom.xml.

## Layout

I don't like to repeat myself, so I use Thymeleaf layouts to keep the copy & pasting down to a minimum. Each page uses
a `layout:fragment="content"` declaration to pull in a layout which includes the standard `<head>`

To see an example of
this, [index.html](https://github.com/wiverson/htmx-demo/blob/master/src/main/resources/templates/index.html)
includes the line `<section layout:fragment="content">` which instructs the
[Thymeleaf Layout Dialect](https://github.com/ultraq/thymeleaf-layout-dialect) to wrap the section with
[layout.html](https://github.com/wiverson/htmx-demo/blob/master/src/main/resources/templates/layout.html).


I originally thought about building a much larger demo, using Spring Security, Spring Data JPA, etc. I decided to back
off on that, as it just added complexity that's unnecessary for the main purpose - showing how htmx works with Spring
Boot.

## Logging

The application.yaml and logback.xml files are set up to dramatically reduce the log noise for a typical Spring Boot
project.

## Screenshots

The mandatory basic web-front end to do list sample app. Here are the
[Java controller](https://github.com/wiverson/htmx-demo/blob/master/src/main/java/com/devhow/htmxdemo/demo/ToDoList.java)
and the [Thymeleaf template](https://github.com/wiverson/htmx-demo/blob/master/src/main/resources/templates/todo.html).
You may notice that there is a very small amount of [hyperscript](https://hyperscript.org) added to the page to address
event edge cases not handled by htmx alone.

![To Do](/www/images/todo.png)

This infinite scroll demo uses the [java-faker](https://github.com/DiUS/java-faker) library to generate an endless
stream of fake data. The more you scroll, the more data you'll see. In this case, the
[Java controller](https://github.com/wiverson/htmx-demo/blob/master/src/main/java/com/devhow/htmxdemo/demo/InfiniteScroll.java)
is just using Java text blocks to return the data. While very, very simple (and fast!) this isn't really a great idea,
in particular due to potential issues around HTML escaping. For most situations you are much better off using either
Thymeleaf templates, [Handlebars/Mustache](https://github.com/jknack/handlebars.java) or [j2html](https://j2html.com/)
for these fragments.

![Infinite Scroll](/www/images/infinite-scroll.png)

The next two screenshots are for a single
[Java controller](https://github.com/wiverson/htmx-demo/blob/master/src/main/java/com/devhow/htmxdemo/demo/InputCatalog.java)
and [Thymeleaf template](https://github.com/wiverson/htmx-demo/blob/master/src/main/resources/templates/input-catalog.html)
.

Every single input immediately posts back data to the controller. In this case, the response is sent back as an element
to append to the messages block, but it's easy to imagine the response updating other elements - or perhaps just
instantly preserving the user's data with no explicit form submit required.

All the widgets shown are just standard HTML elements. A few of them - like file input - can be a bit tricky to handle
correctly - you want to make sure the submission encoding is set right and that you have right configuration on the
server.

Fun fact: the checkbox can only be set to an indeterminate state via JavaScript (in this case, I'm just
using [hyperscript](https://hyperscript.org).

![Standard HTML Input Widgets](/www/images/input-widgets-1.png)

These are mostly various text input widgets. In addition to things like automatically showing special keyboards on
various mobile devices, certain fields such as Search are often used for live updates in response to user typing. There
that functionality can be added trivially, without any JavaScript.

![Standard HTML Input Widgets](/www/images/input-widgets-2.png)
