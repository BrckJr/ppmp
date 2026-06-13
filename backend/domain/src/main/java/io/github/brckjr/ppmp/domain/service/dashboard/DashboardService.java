package io.github.brckjr.ppmp.domain.service.dashboard;

import io.github.brckjr.ppmp.domain.model.dashboard.DashboardSummary;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class DashboardService {

    public DashboardSummary getDashboard() {
        return new DashboardSummary(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                List.of(),
                List.of(),
                List.of(),
                List.of()
        );
    }
}
