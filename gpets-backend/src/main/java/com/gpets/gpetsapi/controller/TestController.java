package com.gpets.gpetsapi.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/api/public/ping")
    public Map<String, String> ping() {
        return Map.of("status", "ok");
    }

    @GetMapping("/api/me")
    public Map<String, String> me() {
        String uid = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Map.of("uid", uid);
    }
}
