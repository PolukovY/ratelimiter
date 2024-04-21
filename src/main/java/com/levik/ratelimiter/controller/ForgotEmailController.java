package com.levik.ratelimiter.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ForgotEmailController {

    @PostMapping
    @RequestMapping("/forgotEmail")
    public void forgotEmail() {

    }
}
