package io.github.brckjr.ppmp.api.dashboard.dto;

import java.math.BigDecimal;

public record AllocationSliceDto(
        String name,
        BigDecimal value
) {}
