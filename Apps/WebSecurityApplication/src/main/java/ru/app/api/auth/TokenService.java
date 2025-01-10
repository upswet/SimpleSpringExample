package ru.app.api.auth;

import com.auth0.jwt.interfaces.DecodedJWT;

/**Проверка токена полученного клиентом у AuthService-а*/
public interface TokenService {
    /** Вернёт раскодирвоанный jwt-токен если он валиден или null если не валиден*/
    DecodedJWT checkToken(String token);
}
