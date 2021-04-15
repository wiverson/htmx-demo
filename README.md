# htmx-demo

Very simple demonstrate of the use of htmx with Spring Boot and Thymeleaf.

Build a Single Page Application (SPA) or just progressively add dynamic JavaScript functionality to your app without a complicated JavaScript framework.

It's possible to be a "full stack" Java developer and provide rich application functionality without complex tools and frameworks.

## Interesting bits

Probably the most interesting bit is the use of webjars to include Bootstrap, jQuery, and htmx. Technically, it's Bootstrap that is looking for jQuery, not htmx.

You can add the WebJar for htmx to your pom.xml like this:

```xml
  <dependency>
      <groupId>org.webjars.npm</groupId>
      <artifactId>htmx.org</artifactId>
      <version>1.3.2</version>
  </dependency>
```

...and then add the following to your application Thymeleaf/HTML to use htmx:

```
<script src="/webjars/jquery/jquery.min.js"></script>
```

And you're off to the races.

## Requirements

- Java 16
- Apache Maven
