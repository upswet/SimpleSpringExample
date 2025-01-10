package ru.app.api.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       /* не нужно проверять так как доступ к разрешённым ресурсам разерешён в конфигурации.
       В данном фильтре проверяем строго токен для доступа к ресурсам требующим авторизации
        if (
                   request.getRequestURI().contains("/public/")
                || request.getRequestURI().contains("/actuator")
                || request.getRequestURI().contains("/swagger-ui")
                || request.getRequestURI().contains("/api-docs")
        ) {
            filterChain.doFilter(request, response);
            return;
        }*/

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader==null || authHeader.isEmpty() || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader == null || authHeader.isBlank())
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        else {
            DecodedJWT decodedJWT = checkAuthorization(authHeader);
            if (decodedJWT==null)
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            else {
                //Авторизация прошла успешно
                //Создадим сприговский контекст безопасности
                //получать как SecurityContextHolder.getContext().getAuthentication()
                //Имя авторизованного пользователя как SecurityContextHolder.getContext().getAuthentication().getName()
                List<SimpleGrantedAuthority> roles = Arrays.stream(decodedJWT.getClaim("roles").asString().split(",")).toList().stream().map(String::trim).map(SimpleGrantedAuthority::new).toList();

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken =new UsernamePasswordAuthenticationToken(decodedJWT.getSubject(), null, roles);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);

                //продолжим обработку цепочки фильтров
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




