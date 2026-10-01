package io.github.brckjr.ppmp.domain.model.transaction;

import io.github.brckjr.ppmp.common.enums.Currency;

import java.math.BigDecimal;

public record TransactionMetrics(
  BigDecimal totalDividends,
  BigDecimal netCapitalInflow,
  BigDecimal totalVolume,
  Currency currency
) {

}
