package io.github.brckjr.ppmp.api.risk.dto;

import java.math.BigDecimal;

public record RiskMetricsDto(
        BigDecimal portfolioVolatilityPct,
        BigDecimal sharpeRatio,
        BigDecimal maxDrawdownPct,
        BigDecimal beta,
        BigDecimal valueAtRisk95Pct,
        String riskScoreLabel
) {}
