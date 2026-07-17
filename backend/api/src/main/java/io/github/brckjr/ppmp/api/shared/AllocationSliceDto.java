package io.github.brckjr.ppmp.api.shared;

import java.math.BigDecimal;

public record AllocationSliceDto(
    String name,
    BigDecimal percentage
) {
}
