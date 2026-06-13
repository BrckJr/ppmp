package io.github.brckjr.ppmp.domain.service.performance;

import io.github.brckjr.ppmp.domain.enums.Currency;
import io.github.brckjr.ppmp.domain.enums.TimeRange;
import io.github.brckjr.ppmp.domain.model.performance.DrawdownPoint;
import io.github.brckjr.ppmp.domain.model.performance.EquityPoint;
import io.github.brckjr.ppmp.domain.model.performance.PerformanceMetrics;
import io.github.brckjr.ppmp.domain.model.performance.RollingVolPoint;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class PerformanceService {

    public PerformanceMetrics getPerformance() {
        return new PerformanceMetrics(
                "Benchmark",
                TimeRange.ONE_Y,
                List.of(new EquityPoint(LocalDate.now(), BigDecimal.ZERO, BigDecimal.ZERO, Currency.USD)),
                List.of(new DrawdownPoint(LocalDate.now(), BigDecimal.ZERO)),
                List.of(new RollingVolPoint(LocalDate.now(), BigDecimal.ZERO))
        );
    }
}
