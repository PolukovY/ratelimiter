package com.levik.ratelimiter.controller;

import com.levik.ratelimiter.controller.dto.UserLoginRequest;
import com.levik.ratelimiter.service.LoginUserStatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.levik.ratelimiter.controller.filter.RateLimitFilter.X_FORWARDED_FOR;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/api")
public class LoginController {

    private final LoginUserStatisticService loginUserStatisticService;

    @PostMapping
    @RequestMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> login(@RequestBody UserLoginRequest userLoginRequest,
                                   @RequestHeader(required = false, name = X_FORWARDED_FOR) String ip) {
        log.info("User {} call with ip {}", userLoginRequest, ip);
        loginUserStatisticService.add(userLoginRequest.username());
        return ResponseEntity.ok().build();

    }

    @GetMapping("/statistic")
    public ResponseEntity<?> statistic() {
        return ResponseEntity.ok(loginUserStatisticService.getStorage());
    }
}
