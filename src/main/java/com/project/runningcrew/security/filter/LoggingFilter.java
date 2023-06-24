package com.project.runningcrew.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();

        return !requestURI.startsWith("/api/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        long start = System.currentTimeMillis();
        filterChain.doFilter(request, response);
        long end = System.currentTimeMillis();

        long times = end - start;
        String method = request.getMethod();
        String queryString = request.getQueryString();
        String uri = queryString == null ? request.getRequestURI() :
                request.getRequestURI() + '?' + URLDecoder.decode(queryString, StandardCharsets.UTF_8);
        String ip = request.getHeader("X-Forwarded-For");
        String user = Objects.requireNonNullElse((String) request.getAttribute("user"), "non-login");

        int status = response.getStatus();

        log.info("\n" +
                        "[REQUEST] {} uri='{}' ip='{}' user='{}'\n" +
                        "[RESPONSE] status={} times={}ms",
                method, uri, ip, user, status, times);
    }

}
