package io.github.brckjr.ppmp.domain.service.risk;

import io.github.brckjr.ppmp.domain.model.risk.RiskMetrics;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;

@ApplicationScoped
public class RiskService {

    public RiskMetrics getRiskMetrics() {
        return new RiskMetrics(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                "UNKNOWN"
        );
    }
}
