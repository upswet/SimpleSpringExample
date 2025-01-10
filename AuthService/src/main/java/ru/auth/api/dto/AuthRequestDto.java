package ru.auth.api.dto;

import lombok.Value;

/**Дто для запросоу аутентификации*/
@Value
public class AuthRequestDto {
    String clientId;
    String audience;
    String clientSecret;
}
