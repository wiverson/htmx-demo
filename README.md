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

Once those are installed, just run `mvn spring-boot:run` and hit [http://localhost:8080](http://localhost:8080)
and you'll see an index page with links to the various demos.

You'll notice that you don't need to install Node.js, npm, or any other tooling.

## Interesting bits



Probably the most interesting bit is the use of webjars to include Bootstrap, jQuery, and htmx. Technically, it's
Bootstrap that is looking for jQuery, not htmx.

You can add the WebJar for [htmx](https://htmx.org/) (and [hyperscript](https://hyperscript.org/) as well) to your
pom.xml like this:

```xml

<dependencies>
    <dependency>
        <groupId>org.webjars.npm</groupId>
        <artifactId>htmx.org</artifactId>
        <version>1.3.3</version>
    </dependency>
    <dependency>
        <groupId>org.webjars.npm</groupId>
        <artifactId>hyperscript.org</artifactId>
        <version>0.0.9</version>
    </dependency>
</dependencies>
```

...and then add the following to your application Thymeleaf/HTML to use htmx:

```xml

<script type="text/javascript" th:src="@{/webjars/htmx.org/dist/htmx.min.js}"></script>
```

And you're off to the races!

## Minor Notes

I originally thought about building a much larger demo, using Spring Security, Spring Data JPA, etc. I decided to back
off on that, as it just added complexity that's unnecessary for the main purpose - showing how htmx works with Spring
Boot.

Aside from adding a simple Controller and some templates, the most interesting parts of this project are likely the
application.yaml and logback.xml files, which are set up to dramatically reduce the log noise for a typical Spring Boot
project.

If you think a more complicated demo app would be useful, let me know.

## Requirements

- Java 16
- Apache Maven

That's it. No npm, no JavaScript frameworks, no REST services generating JSON that is then converted back to HTML in the
browser.

If you want to be fancy, tell folks that you
are [migrating back to server-side rendering for your SPA applications](https://blog.asayer.io/server-side-rendering-ssr-with-react)
for performance and security reasons.

## Screenshots

The mandatory basic web-front end to do list sample app. Here are the
[Java controller](https://github.com/wiverson/htmx-demo/blob/master/src/main/java/com/devhow/htmxdemo/demo/ToDoList.java) 
and the [Thymeleaf template](https://github.com/wiverson/htmx-demo/blob/master/src/main/resources/templates/todo.html).

![To Do](/www/images/todo.png)

![Infinite Scroll](/www/images/infinite-scroll.png)

![Standard HTML Input Widgets](/www/images/input-widgets-1.png)

![Standard HTML Input Widgets](/www/images/input-widgets-2.png)
