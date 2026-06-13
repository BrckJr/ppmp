package io.github.brckjr.ppmp.domain.model.watchlist;

import io.github.brckjr.ppmp.domain.enums.AnalystRating;

import java.math.BigDecimal;

public record WatchlistItemView(
        String ticker,
        String name,
        BigDecimal price,
        BigDecimal targetPrice,
        AnalystRating analystRating,
        BigDecimal pe,
        BigDecimal pegRatio,
        BigDecimal dividendYieldPct
) {}
