package ru.app.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.app.db.entity.AbstractEntity;

/**Разные утилиты для работы с бд*/
@Service
@Slf4j
@RequiredArgsConstructor
public class Utils {
    /**Вернёт первый не нулевой объект из переданных*/
    public static <T> T coalesce(T... items) {
        for (T i : items) if (i != null) return i;
        return null;
    }
}
