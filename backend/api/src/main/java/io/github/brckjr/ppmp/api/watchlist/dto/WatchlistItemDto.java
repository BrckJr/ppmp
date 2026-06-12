package io.github.brckjr.ppmp.api.watchlist.dto;

import io.github.brckjr.ppmp.domain.enums.AnalystRating;

import java.math.BigDecimal;

public record WatchlistItemDto(
        String ticker,
        String name,
        BigDecimal price,
        BigDecimal targetPrice,
        AnalystRating analystRating,
        BigDecimal pe,
        BigDecimal pegRatio,
        BigDecimal dividendYieldPct
) {}
