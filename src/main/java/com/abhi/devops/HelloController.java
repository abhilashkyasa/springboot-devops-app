package com.abhi.devops;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    private final String appVersion;

    public HelloController(@Value("${app.version:local}") String appVersion) {
        this.appVersion = appVersion;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello Abhi! Spring Boot DevOps CI/CD Application is running successfully.";
    }

    /** Lets the demo audience see which build/image tag is live after each pipeline run. */
    @GetMapping("/version")
    public Map<String, String> version() {
        return Map.of("app", "springboot-devops-app", "version", appVersion);
    }
}
