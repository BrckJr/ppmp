package io.github.brckjr.ppmp.api.dashboard.dto;

import io.github.brckjr.ppmp.api.performance.dto.EquityPointDto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardDto(
        BigDecimal portfolioValue,
        BigDecimal portfolioValuePct,
        BigDecimal dailyPL,
        BigDecimal dailyPLPct,
        BigDecimal totalReturn,
        BigDecimal totalReturnPct,
        BigDecimal annualizedReturnPct,
        BigDecimal cashPosition,
        BigDecimal totalInvested,
        List<EquityPointDto> equityCurve,
        List<AllocationSliceDto> allocationByAssetClass,
        List<AllocationSliceDto> allocationBySector,
        List<AllocationSliceDto> allocationByRegion
) {}
