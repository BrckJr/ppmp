package io.github.brckjr.ppmp.api.performance.dto;

import io.github.brckjr.ppmp.domain.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EquityPointDto(
        LocalDate date,
        BigDecimal value,
        BigDecimal benchmarkValue,
        Currency currency
) {
}
