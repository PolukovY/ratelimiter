package com.levik.ratelimiter.config;

import com.levik.ratelimiter.service.UserTokenBucketService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RateLimiterConfiguration {

    @Bean
    public UserTokenBucketService userTokenBucketService(
            @Value("${rateLimit.userTokenBucket.maxBucketSizePerMinute:2}") Integer maxBucketSizePerMinute) {
        return new UserTokenBucketService(new ConcurrentHashMap<>(), maxBucketSizePerMinute);
    }
}
