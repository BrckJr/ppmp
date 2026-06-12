package io.github.brckjr.ppmp.api.performance.dto;

import java.math.BigDecimal;

public record EquityPointDto(
        String date,
        BigDecimal value,
        BigDecimal benchmark
) {}
