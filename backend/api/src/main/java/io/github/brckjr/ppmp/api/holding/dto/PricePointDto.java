package io.github.brckjr.ppmp.api.holding.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PricePointDto(
        LocalDate date,
        BigDecimal price
) {}
