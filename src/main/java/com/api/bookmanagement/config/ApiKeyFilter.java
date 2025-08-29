package com.api.bookmanagement.config;

import com.api.bookmanagement.util.GlobalConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiKeyFilter extends OncePerRequestFilter {

    private final Messages messages;

    @Value("${api.key}")
    private String expectedApiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String apiKey = request.getHeader("api-key");

        if (apiKey == null || !apiKey.equals(expectedApiKey)) {
            log.error("Invalid or missing API key");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"error\":\"" + messages.get(GlobalConstants.ERROR_INVALID_API_KEY) + "\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
