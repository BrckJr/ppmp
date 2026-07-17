package io.github.brckjr.ppmp.api.shared;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EquityPointDto(
    LocalDate date,
    BigDecimal value,
    String currency
) {
}