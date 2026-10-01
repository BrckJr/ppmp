package io.github.brckjr.ppmp.api.dashboard.dto;

import java.math.BigDecimal;

public record KpiMetricsDto(
    BigDecimal totalPortfolioValue,
    BigDecimal totalPortfolioReturnPct,
    BigDecimal dailyTotalPortfolioPL,
    BigDecimal dailyTotalPortfolioPLPct,
    BigDecimal totalPortfolioGain,
    BigDecimal totalPortfolioAnnualizedReturn,
    BigDecimal totalPortfolioCashPosition,
    BigDecimal totalPortfolioInitialInvested
) {
}