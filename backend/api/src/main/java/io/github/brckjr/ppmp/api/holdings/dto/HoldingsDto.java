package io.github.brckjr.ppmp.api.holdings.dto;

import java.util.List;

public record HoldingsDto(
        List<HoldingDetailDto> holdings
) {}
