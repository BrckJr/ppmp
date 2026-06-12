package io.github.brckjr.ppmp.api.dashboard.dto;

import io.github.brckjr.ppmp.api.performance.dto.EquityPointDto;

import java.math.BigDecimal;
import java.util.List;

public record PortfolioOverviewDto(
        BigDecimal portfolioValue,
        BigDecimal dailyPL,
        BigDecimal dailyPLPct,
        BigDecimal totalGain,
        BigDecimal totalReturnPct,
        BigDecimal annualizedReturnPct,
        BigDecimal cashPosition,
        BigDecimal totalInvested,
        List<AllocationSliceDto> allocationByAssetClass,
        List<AllocationSliceDto> allocationBySector,
        List<AllocationSliceDto> allocationByRegion,
        List<EquityPointDto> equityCurve
) {}
