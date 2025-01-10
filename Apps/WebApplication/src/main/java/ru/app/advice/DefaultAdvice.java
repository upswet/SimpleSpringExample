package ru.app.advice;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.app.api.dto.AnswerDto;

/**Перехват и пользовательская обработка исключений*/
@ControllerAdvice
@Slf4j
public class DefaultAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<AnswerDto> handleException(Exception e) {
        log.error("Перехвачена ошибка: ",e);

        AnswerDto response = new AnswerDto();
        response.setErrorText(e.getClass().getName()+": "+e.getLocalizedMessage()+(e.getCause()!=null ? " ("+e.getCause().toString()+")" : ""));
        response.setErrorType(e.getClass().getSimpleName());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
