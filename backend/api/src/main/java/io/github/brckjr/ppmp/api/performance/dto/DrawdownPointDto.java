package io.github.brckjr.ppmp.api.performance.dto;

import java.math.BigDecimal;

public record DrawdownPointDto(
        String date,
        BigDecimal dd
) {}
