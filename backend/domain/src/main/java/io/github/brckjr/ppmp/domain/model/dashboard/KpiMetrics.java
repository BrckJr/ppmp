package io.github.brckjr.ppmp.domain.model.dashboard;

import java.math.BigDecimal;

public record KpiMetrics(
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
