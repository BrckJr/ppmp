package io.github.brckjr.ppmp.domain.service.dashboard;

import io.github.brckjr.ppmp.domain.model.dashboard.DashboardSummary;
import io.github.brckjr.ppmp.domain.model.dashboard.KpiMetrics;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class DashboardService {

  public DashboardSummary getDashboard() {
    KpiMetrics kpis = new KpiMetrics(
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO
    );

    return new DashboardSummary(
        kpis,
        List.of(),
        List.of(),
        List.of(),
        List.of()
    );
  }
}
