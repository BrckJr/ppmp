package io.github.brckjr.ppmp.domain.model.performance;

import io.github.brckjr.ppmp.domain.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EquityPoint(
        LocalDate date,
        BigDecimal value,
        BigDecimal benchmarkValue,
        Currency currency
) {}
