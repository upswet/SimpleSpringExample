package ru.auth.api.dto;

import lombok.Value;

/**Дто для ошибочного ответа*/
@Value
public class ErrorResponseDto {
    String message;
}
