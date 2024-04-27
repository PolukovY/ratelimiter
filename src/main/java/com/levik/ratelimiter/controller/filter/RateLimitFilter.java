package com.levik.ratelimiter.controller.filter;

import com.levik.ratelimiter.redis.DistributedRateLimiterClient;
import com.levik.ratelimiter.service.UserTokenBucketService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    public static final String RATE_LIMIT_EXCEEDED_TRY_LATER = "Rate limit exceeded. Try later...";
    public static final String X_RATE_LIMIT_RETRY_AFTER_SECONDS = "X-Rate-Limit-Retry-After-Seconds";

    private final UserTokenBucketService userTokenBucketService;

    private final DistributedRateLimiterClient distributedRateLimiterClient;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
                                    FilterChain filterChain) throws ServletException, IOException {
        String remoteAddr = req.getHeader(X_FORWARDED_FOR);
        String requestURI = req.getRequestURI();

        if (requestURI.contains("/login")) {
            //routing for showing different algorithms Token Bucket

            if (Objects.nonNull(remoteAddr) && userTokenBucketService.allowedRequest(remoteAddr)) {
                filterChain.doFilter(req, res);
                return;
            }
        } else if (requestURI.contains("/forgotEmail")) {
            //Will be using annotation approach for demo purpose
            filterChain.doFilter(req, res);
            return;
        } else if (requestURI.contains("/notification")) {

            var requestAllowed = distributedRateLimiterClient.isRequestAllowed(remoteAddr, 1);

            if (requestAllowed.isConsumed()) {
                filterChain.doFilter(req, res);
                return;
            }

            res.setHeader(X_RATE_LIMIT_RETRY_AFTER_SECONDS, "" + TimeUnit.NANOSECONDS.toSeconds(requestAllowed.getNanosToWaitForRefill()));
        }



        res.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        res.getWriter().write(RATE_LIMIT_EXCEEDED_TRY_LATER);
        res.getWriter().flush();
    }
}
