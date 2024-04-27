package com.levik.ratelimiter.service;

import java.util.concurrent.atomic.AtomicInteger;

public class TokenBucket {
    private static final int SECOND_IN_MINUTES = 60;
    private static final int ONE_MINUTES_TO_MILLISECONDS = SECOND_IN_MINUTES * 1000;

    private final int maxBucketSizePerMinute;
    private final AtomicInteger currentBucketSize;
    private volatile long lastRefillTimestamp;

    public TokenBucket(int maxBucketSizePerMinute) {
        this.maxBucketSizePerMinute = maxBucketSizePerMinute;

        this.currentBucketSize = new AtomicInteger(maxBucketSizePerMinute);
        this.lastRefillTimestamp = System.currentTimeMillis();
    }

    public boolean allowedRequest() {
        refill();

        int currentSize = currentBucketSize.get();
        do {
            if (currentSize <= 0) {
                return false;
            }

            currentSize = currentBucketSize.get();
        } while (!currentBucketSize.compareAndSet(currentSize, currentSize - 1));

        return true;
    }

    private void refill() {
        long timeElapsedSinceLastRefill = System.currentTimeMillis() - lastRefillTimestamp;

        if (timeElapsedSinceLastRefill >= ONE_MINUTES_TO_MILLISECONDS) {
            synchronized (this) {
                if (timeElapsedSinceLastRefill >= ONE_MINUTES_TO_MILLISECONDS) {
                    this.currentBucketSize.set(maxBucketSizePerMinute);
                    this.lastRefillTimestamp = System.currentTimeMillis();
                }
            }
        }
    }
}
