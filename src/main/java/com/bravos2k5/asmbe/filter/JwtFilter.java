package com.bravos2k5.asmbe.filter;

import com.bravos2k5.asmbe.ResponseCodeCustom;
import com.bravos2k5.asmbe.dto.UserInfo;
import com.bravos2k5.asmbe.service.JwtService;
import com.bravos2k5.asmbe.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    public JwtFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws IOException {

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            String accessToken = authHeader.substring(7);

            if (jwtService.isExpiredToken(accessToken)) {
                writeErrorResponse(response, ResponseCodeCustom.SC_TOKEN_IS_EXPIRED, "Token is expired");
                return;
            }

            UserInfo userInfo = userService.getUserInfoFromToken(accessToken);
            if (userInfo == null) {
                writeErrorResponse(response, ResponseCodeCustom.SC_INVALID_TOKEN, "Invalid token");
                return;
            }

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    userInfo.username(), null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(token);

            filterChain.doFilter(request, response);
        } catch (ServletException e) {
            writeErrorResponse(response, ResponseCodeCustom.SC_BAD_GATEWAY, "Internal server error");
        }
    }

    private void writeErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setContentType("application/json");
        response.setStatus(statusCode);

        String jsonResponse = String.format("{\"status\":%d,\"error\":\"%s\"}", statusCode, message);
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }

}
