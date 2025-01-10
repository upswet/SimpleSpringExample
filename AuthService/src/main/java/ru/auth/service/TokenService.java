package ru.auth.service;

//https://jwt.io/

import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.List;

/**Генерация jwt-токена для клиента*/
public interface TokenService {
    String generateAccessToken(String clientId, String audience, String roles);
    String generateRefreshToken(String clientId, String audience);

    Boolean checkAccessToken(String accessToken);
    Boolean checkRefreshToken(String refreshToken);
    DecodedJWT decodeRefreshToken(String refreshToken);

    }
