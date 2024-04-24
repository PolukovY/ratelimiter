package com.levik.ratelimiter.redis;

import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@RequiredArgsConstructor
@Slf4j
@Service
public class DistributedRateLimiterClient {

    private final Supplier<BucketConfiguration> bucketConfiguration;
    private final ProxyManager<String> proxyManager;

    public ConsumptionProbe isRequestAllowed(String key, long numberTokens) {
        var bucket = proxyManager.builder().build(key, bucketConfiguration);
        var probe = bucket.tryConsumeAndReturnRemaining(numberTokens);
        log.info(">>>>>>>>remainingTokens: key {} : {}", key, probe.getRemainingTokens());
        return probe;
    }
}
