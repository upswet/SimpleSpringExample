package ru.auth.api.dto;

import lombok.*;

/**Дто для ответа на запрос аутентифиакции*/
@Value
public class AuthResponseDto {
    private final String type = "Bearer";
    String accessToken;
    String refreshToken;
}
