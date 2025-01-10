package ru.app.api.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**Фильтр проверяющий ацесс-токен для авторизации который надо получать у auth-service если в настройках включена авторизация*/
@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    @Value("${auth.enabled}")
    private boolean enabled;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!enabled
                || request.getMethod().equals(HttpMethod.OPTIONS.name())
                || request.getRequestURI().contains("/public/")
                || request.getRequestURI().contains("/h2-console")
                || request.getRequestURI().contains("/actuator")
                || request.getRequestURI().contains("/swagger-ui")
                || request.getRequestURI().contains("/api-docs")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || authHeader.isBlank())
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        else {
            DecodedJWT decodedJWT = checkAuthorization(authHeader);
            if (decodedJWT==null)
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            else {
                //Авторизация прошла успешно
                //заполним контекст запроса
                //получать как RequestContextHolder.getRequestAttributes().getAttribute("roles", 0);
                RequestContextHolder.getRequestAttributes().setAttribute("clientId", decodedJWT.getSubject(), 0);
                RequestContextHolder.getRequestAttributes().setAttribute("roles", Arrays.stream(decodedJWT.getClaim("roles").asString().split(",")).toList().stream().map(String::trim).toList(), 0);

                filterChain.doFilter(request, response);
            }
        }
    }

    private DecodedJWT checkAuthorization(String auth) {
        if (!auth.startsWith("Bearer "))
            return null;

        String token = auth.substring(7);
        return tokenService.checkToken(token);
    }
}
