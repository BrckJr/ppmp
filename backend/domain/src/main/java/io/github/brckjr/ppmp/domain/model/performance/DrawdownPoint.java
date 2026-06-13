package io.github.brckjr.ppmp.domain.model.performance;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DrawdownPoint(
        LocalDate date,
        BigDecimal dd
) {}
