package com.levik.ratelimiter.controller;

import com.levik.ratelimiter.annotation.RateLimitProtection;
import com.levik.ratelimiter.controller.dto.NotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/api")
public class NotificationController {

    @RateLimitProtection
    @PostMapping
    @RequestMapping("/notification")
    public void notification(@RequestBody NotificationDto notificationDto) {
        log.info("Call notification {}... ", notificationDto);
    }
}
