package io.github.brckjr.ppmp.domain.model.risk;

import java.math.BigDecimal;

public record RiskMetrics(
    BigDecimal portfolioVolatilityPct,
    BigDecimal sharpeRatio,
    BigDecimal maxDrawdownPct,
    BigDecimal beta,
    BigDecimal valueAtRisk95Pct,
    String riskScoreLabel
) {
}
