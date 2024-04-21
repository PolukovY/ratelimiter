package com.levik.ratelimiter.service;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class LoginUserStatisticService {
    @Getter
    private final Map<String, Integer> storage = new ConcurrentHashMap<>();

    public void add(String username) {
        storage.compute(username, (key, oldValue) -> oldValue == null ? 1 : oldValue + 1);
    }


}
