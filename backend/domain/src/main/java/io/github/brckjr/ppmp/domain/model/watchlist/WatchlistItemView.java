package io.github.brckjr.ppmp.domain.model.watchlist;

import io.github.brckjr.ppmp.common.enums.AnalystRating;

import java.math.BigDecimal;
import java.util.UUID;

public record WatchlistItemView(
    UUID id,
    UUID instrumentId,
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
