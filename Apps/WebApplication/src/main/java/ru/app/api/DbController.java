package ru.app.api;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.app.api.dto.AnswerDto;
import ru.app.db.entity.AbstractEntity;
import ru.app.db.entity.WorkerEntity;

@RestController
@Tag(name = "Примеры работы с БД", description = "Объединяет апи написанные в качестве примеров для работы с бд через хибернейт",
        externalDocs = @ExternalDocumentation(
                description = "Ссылка на общую документацию",
                url = "https://example.com/docs/user-controller"
        )
)
@RequestMapping("/app/db/worker")
@Slf4j
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
public class DbController {
    @Operation(summary = "Получить сотрудника")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "успешно"),
            @ApiResponse(responseCode = "500", description = "внутренняя ошибка")
    })
    @GetMapping("{id}")
    public AnswerDto findById(@PathVariable("id")  Long workerId) {
        return new AnswerDto().setData(AbstractEntity.findById(WorkerEntity.class, workerId));
    }

    @Operation(summary = "Удалить сотрудника")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "успешно"),
            @ApiResponse(responseCode = "500", description = "внутренняя ошибка")
    })
    @DeleteMapping("{id}")
    @Transactional
    public AnswerDto delete(@PathVariable("id")  Long workerId) {
        AbstractEntity.delete(AbstractEntity.findById(WorkerEntity.class, workerId));
        return new AnswerDto().setData("Успешно удалено");
    }

    @Operation(summary = "Получить всех сотрудников")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "успешно"),
            @ApiResponse(responseCode = "500", description = "внутренняя ошибка")
    })
    @GetMapping("")
    public AnswerDto findByAll() {
        return new AnswerDto().setData(AbstractEntity.findForQuery("select we from WorkerEntity we",false, WorkerEntity.class));
    }
}
