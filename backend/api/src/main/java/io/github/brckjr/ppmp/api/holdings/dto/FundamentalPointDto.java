package io.github.brckjr.ppmp.api.holdings.dto;

import java.math.BigDecimal;

public record FundamentalPointDto(
        String year,
        BigDecimal revenue,
        BigDecimal netIncome,
        BigDecimal fcf
) {}
