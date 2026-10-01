package io.github.brckjr.ppmp.domain.model.shared;

import io.github.brckjr.ppmp.common.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EquityPoint(
    LocalDate date,
    BigDecimal value,
    Currency currency
) {
}
