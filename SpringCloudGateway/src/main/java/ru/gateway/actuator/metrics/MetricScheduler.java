package ru.gateway.actuator.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**Шедуллер для обработки кастомных меток
 * https://habr.com/ru/companies/otus/articles/650871/
 * Из MeterRegistry можно инстанцировать следующие типы счетчиков:
 * Counter: сообщает только о результатах подсчета указанного свойства приложения.
 * Gauge: показывает текущее значение измерительного прибора
 * Timers: измеряет задержки или частоту событий
 * DistributionSummary: обеспечивает дистрибуцию событий и простую итоговую сводку.*/
@Component
public class MetricScheduler {
    private final AtomicInteger testGauge;
    private final Counter testCounter;

    public MetricScheduler(MeterRegistry meterRegistry) {
        // Counter vs. gauge, summary vs. histogram
        // https://prometheus.io/docs/practices/instrumentation/#counter-vs-gauge-summary-vs-histogram
        testGauge = meterRegistry.gauge("custom_gauge", new AtomicInteger(0));
        testCounter = meterRegistry.counter("custom_counter");
    }

    @Scheduled(fixedRateString = "1000", initialDelayString = "0")
    public void schedulingTask() {
        testGauge.set(getRandomNumberInRange(0, 100));
        testCounter.increment();
    }

    private static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
