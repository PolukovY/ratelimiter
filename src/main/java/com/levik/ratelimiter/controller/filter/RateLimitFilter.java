package com.levik.ratelimiter.controller.filter;

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

@Slf4j
@RequiredArgsConstructor
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    public static final String X_REAL_IP = "X-Real-IP";
    public static final String RATE_LIMIT_EXCEEDED_TRY_LATER = "Rate limit exceeded. Try later...";

    private final UserTokenBucketService userTokenBucketService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
                                    FilterChain filterChain) throws ServletException, IOException {
        String remoteAddr = req.getHeader(X_REAL_IP);
        String requestURI = req.getRequestURI();

        if (requestURI.contains("/login")) {
            //routing for showing different algorithms Token Bucket

            if (Objects.nonNull(remoteAddr) && userTokenBucketService.allowedRequest(remoteAddr)) {
                filterChain.doFilter(req, res);
                return;
            }
        }



        res.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        res.getWriter().write(RATE_LIMIT_EXCEEDED_TRY_LATER);
        res.getWriter().flush();
    }
}
