package com.levik.ratelimiter.service;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class TokenBucketTest {

    /***
     * What we can see in this example that this algo can exceed defined limit.
     *
     * Summary
     *
     * The Time Bucket rate limiter algorithm is easy to understand and implement.
     * Also, it is memory efficient because it only needs a fixed-sized memory bucket.
     * But it might exceed the rate limit during the same time window.
     *
     * @throws InterruptedException
     */
    @Test
    void shouldDemoTokenBucket() throws InterruptedException {
        TokenBucketDemo tokenBucketDemo = new TokenBucketDemo(10, 10);
        assertThat(tokenBucketDemo.allowRequest(5)).isTrue();
        assertThat(tokenBucketDemo.allowRequest(2)).isTrue();
        TimeUnit.MILLISECONDS.sleep(200);
        assertThat(tokenBucketDemo.allowRequest(5)).isTrue();
    }

}