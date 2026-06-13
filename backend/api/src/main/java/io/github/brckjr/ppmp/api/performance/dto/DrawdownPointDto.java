package io.github.brckjr.ppmp.api.performance.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DrawdownPointDto(
        LocalDate date,
        BigDecimal dd
) {
}
