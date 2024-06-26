package com.levik.ratelimiter.controller;

import com.levik.ratelimiter.annotation.RateLimitProtection;
import com.levik.ratelimiter.controller.dto.ForgotEmailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/api")
public class ForgotEmailController {

    @RateLimitProtection
    @PostMapping
    @RequestMapping("/forgotEmail")
    public void forgotEmail(@RequestBody ForgotEmailDto forgotEmailDto) {
        log.info("Call forgotEmail {}... ", forgotEmailDto);
    }
}
