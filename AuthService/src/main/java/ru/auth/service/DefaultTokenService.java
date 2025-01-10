package ru.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class DefaultTokenService implements TokenService {
    @Value("${auth.jwt.secret.access}")
    private String secretAccessKey;
    @Value("${auth.jwt.secret.refresh}")
    private String secretRefreshKey;


    /**Сгенерировать токен для клиента
     * @param clientId - клиент для которого генерируем токен
     * @param secretKey - секретный ключ на основе которого будем его генерировать
     * @param minuts - через сколько минут истечёт срок действия токена
     * @param audience - для какой системы генерируем токен
     * @param roles - список ролей доступных текущему пользователю для данной системы чере запятую
     * @return - сгенерированный токен
     *  */
    private String generateToken(String clientId, String secretKey, Long minuts, String audience, String roles) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        Instant now = Instant.now();
        Instant exp = now.plus(minuts, ChronoUnit.MINUTES);

        return JWT.create()
                .withIssuer("auth-service")
                .withClaim("roles",roles)
                .withAudience(audience)
                .withSubject(clientId)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(exp))
                .sign(algorithm);
    }

    @Override
    public String generateAccessToken(String clientId, String audience, String roles) {
        return generateToken(clientId, secretAccessKey, 5L, audience, roles);
    }

    @Override
    public String generateRefreshToken(String clientId, String audience) {
        return generateToken(clientId, secretRefreshKey, 666L, audience, null);
    }


    /**Сгенерировать токен для клиента
     * @param token - проверяемый токен
     * @param secretKey - секретный ключ которым он был закодирован
     * @return - истина, если токен валиден и ложь если не валиден*/
    private boolean checkToken(String token, String secretKey) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();

        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            log.error("Token is invalid: " + e.getMessage());
            return false;
        }
    }

    /**Декодировать токен
     * @param refreshToken - рефреш-токен
     * @return - декодированный токен или исключение*/
    @Override
    public DecodedJWT decodeRefreshToken(String refreshToken) {
        Algorithm algorithm = Algorithm.HMAC256(secretAccessKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(refreshToken);
    }

    @Override
    public Boolean checkAccessToken(String accessToken) {
        return checkToken(accessToken, secretAccessKey);
    }

    @Override
    public Boolean checkRefreshToken(String refreshToken) {
        return checkToken(refreshToken, secretRefreshKey);
    }
}
