package io.github.brckjr.ppmp.api.transactions.dto;

import io.github.brckjr.ppmp.common.annotation.Required;

import java.math.BigDecimal;

public record TransactionMetricsDto(
  @Required BigDecimal totalDividends,
  @Required BigDecimal netCapitalInflow,
  @Required BigDecimal totalVolume,
  @Required String currency
) {
}
