package com.boilerplate.security.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.boilerplate.modules.user.domain.User;
import com.boilerplate.modules.user.service.IUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider tokenProvider;
    @Autowired
    private IUserService userService;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthTokenFilter.class);

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {

            String jwt = "";
            if(request.getRequestURI().endsWith("/api/auth/refresh-token")){
                jwt = getJwt(request, "refreshToken");
            } else {
                jwt = getJwt(request, "accessToken");
            }
            if (Objects.nonNull(jwt) && tokenProvider.validateJwtToken(jwt)) {
                String username = tokenProvider.getUserNameFromJwtToken(jwt);

                User user = (User) userService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (Exception e) {
            logger.error("Can NOT set user authentication -> {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String getJwt(HttpServletRequest request, String name) {
        Map<String, String> cookies = new HashMap<String, String>();
        if (request.getCookies() != null && request.getCookies().length > 0) {
            Arrays.stream(request.getCookies()).forEach(
                    cookie -> {
                        cookies.put(cookie.getName(), cookie.getValue());
                    }
            );
        }
        return cookies.get(name);
        //return request.getHeader(name);
    }
}
