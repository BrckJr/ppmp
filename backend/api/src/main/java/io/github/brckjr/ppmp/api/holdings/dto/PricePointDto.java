package io.github.brckjr.ppmp.api.holdings.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PricePointDto(
        LocalDate date,
        BigDecimal price
) {}
