package io.github.brckjr.ppmp.api.watchlist.dto;


import io.github.brckjr.ppmp.common.annotation.Required;
import io.github.brckjr.ppmp.common.enums.AnalystRating;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record WatchlistItemDto(
    UUID id,
    @Required @NotNull UUID instrumentId,
    String ticker,
    String name,
    BigDecimal price,
    BigDecimal targetPrice,
    AnalystRating analystRating,
    BigDecimal pe,
    BigDecimal pegRatio,
    BigDecimal dividendYieldPct
) {
}
