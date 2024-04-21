package com.levik.ratelimiter.service;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class UserTokenBucketService {

    private final Map<String, TokenBucket> userTokenBucket;
    private final int maxBucketSizePerMinute;

    public boolean allowedRequest(String username) {
        TokenBucket tokenBucket = userTokenBucket.getOrDefault(username, new TokenBucket(maxBucketSizePerMinute));
        boolean isAllowedRequest;

        synchronized (username) {
            isAllowedRequest = tokenBucket.allowedRequest();
            userTokenBucket.put(username, tokenBucket);
        }

        return isAllowedRequest;
    }
}
