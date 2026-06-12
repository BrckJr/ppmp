package io.github.brckjr.ppmp.api.performance.dto;

import java.math.BigDecimal;

public record RollingVolPointDto(
        String date,
        BigDecimal vol
) {}
