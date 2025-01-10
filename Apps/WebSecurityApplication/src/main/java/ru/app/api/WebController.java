package ru.app.api;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.app.service.MyService;

@RestController
@Tag(name = "Название контроллера", description = "Описание контроллера",
        externalDocs = @ExternalDocumentation(
            description = "Ссылка на общую документацию",
            url = "https://example.com/docs/user-controller"
        )
)
@RequestMapping("/app/v1")
@Slf4j
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
public class WebController {
    private final MyService service;

    @Operation(summary = "Хэллоу1-апи", description = "пример простого тестового апи")
    @GetMapping("/hello1")
    public String hello1() {
        return "Hello_1";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Хэллоу2-апи", description = "пример простого тестового апи")
    @GetMapping("/hello2")
    public String hello2() {
        return "Hello_2";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Хэллоу3-апи", description = "пример простого тестового апи")
    @GetMapping("/hello3")
    public String hello3() {
        return service.helloService();
    }
}
