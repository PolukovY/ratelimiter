package com.levik.ratelimiter.redis;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.function.Supplier;

import static io.github.bucket4j.distributed.ExpirationAfterWriteStrategy.*;

@Configuration
public class RedisConfiguration {

    @Value("${redis.refilling.bucket.UpToMaxMinutes:1}")
    private long refillingBucketUpToMaxMinutes;

    @Value("${redis.refilling.bucket.MaxRequests:3}")
    private long refillingBucketMaxRequests;

    @Value("${redis.host:localhost}")
    private String host;

    @Value("${redis.port:6379}")
    private int port;

    @Value("${redis.ssl:false}")
    private boolean ssl;

    @Bean
    public RedisClient redisClient() {
        return RedisClient.create(RedisURI.builder()
                .withHost(host)
                .withPort(port)
                .withSsl(ssl)
                .build());
    }

    @Bean
    public ProxyManager<String> lettuceBasedProxyManager(RedisClient redisClient) {
        StatefulRedisConnection<String, byte[]> redisConnection = redisClient
                .connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));

        return LettuceBasedProxyManager.builderFor(redisConnection)
                .withExpirationStrategy(basedOnTimeForRefillingBucketUpToMax(Duration.ofMinutes(refillingBucketUpToMaxMinutes)))
                .build();
    }

    @Bean
    public Supplier<BucketConfiguration> bucketConfiguration() {
        var bandwidth = Bandwidth.builder()
                .capacity(refillingBucketMaxRequests)
                .refillIntervally(1, Duration.ofMinutes(refillingBucketUpToMaxMinutes))
                .build();
        return () -> BucketConfiguration.builder()
                .addLimit(bandwidth)
                .build();
    }
}
