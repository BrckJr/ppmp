package io.github.brckjr.ppmp.api.performance.dto;

import io.github.brckjr.ppmp.common.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EquityPointDto(
    LocalDate date,
    BigDecimal value,
    Currency currency
) {
}
