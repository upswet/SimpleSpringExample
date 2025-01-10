package ru.app.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description ="Стандартизированное дто-ответа")
public class AnswerDto {
    @Schema(description = "Текст ошибки, если она произошла", example = "Ошибка такая-то")
    String errorText;
    @Schema(description = "Тип ошибки, если она произошла", example = "ArithmeticException")
    String errorType;
    @Schema(description = "Данные ответа в случае успешной обработки запроса")
    Object data;
}
