package ru.app.api;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.app.api.auth.AuthorizationService;
import ru.app.api.dto.AnswerDto;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

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
@RefreshScope
public class WebController {
    @Value("${myValue:none}")
    private String myValue;

    @Operation(summary = "Хэллоу-апи", description = "пример простого тестового апи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "успешно")
    })
    @GetMapping("/hello")
    public AnswerDto hello(@RequestParam(required = false) String name) {

        /*Thread thread = new Thread(() -> {
            AuthorizationService.checkRole("ROLE_WEB");
        });
        thread.start();*/

        AuthorizationService.checkRole("ROLE_WEB");
        return new AnswerDto().setData("Hello, " + name+" "+myValue);
    }

    @Operation(summary = "Пример апи получающего и отправляющего файлы")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "успешно")
    })
    @PostMapping(value = "/file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Resource> workWithFile(@RequestPart("file") MultipartFile file, @RequestParam Map<String, String> allParams) {
        MediaType contentType = MediaType.valueOf(file.getContentType());

        StringBuilder sb = new StringBuilder();
        sb.append("Параметры которые вы прислали: "+allParams.toString());
        sb.append("\nИнформация по присланному файлу");
        sb.append("\nИмя файла: "+file.getOriginalFilename());
        sb.append("\nМедиа-тип файла: "+contentType.toString());
        //file.getInputStream().readAllBytes().toString()

        Resource fileResource = new InputStreamResource(new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8)));
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_MARKDOWN)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "answer.txt" + "\"")
                    .body(fileResource);

    }

    @Operation(summary = "Ошибка-апи", description = "Демонстрирует обработку ошибок")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "успешно"),
            @ApiResponse(responseCode = "500", description = "внутренняя ошибка")
    })
    @GetMapping(value = "/error")
    public AnswerDto error() {
        return new AnswerDto().setData(10/0);
    }
}
