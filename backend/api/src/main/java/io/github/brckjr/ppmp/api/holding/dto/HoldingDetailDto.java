package io.github.brckjr.ppmp.api.holding.dto;

public record HoldingDetailDto(
        HoldingDto holding,
        List<PricePointDto> priceHistory,
        List<FundamentalPointDto> fundamentals
) {}
