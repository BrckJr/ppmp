package io.github.brckjr.ppmp.api.holdings.dto;

import java.util.List;

public record HoldingDetailDto(
        HoldingDto holding,
        List<PricePointDto> priceHistory,
        List<FundamentalPointDto> fundamentals
) {}
