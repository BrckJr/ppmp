package io.github.brckjr.ppmp.domain.model.dashboard;

import io.github.brckjr.ppmp.domain.model.performance.EquityPoint;
import io.github.brckjr.ppmp.domain.model.portfolio.AssetAllocationSlice;
import io.github.brckjr.ppmp.domain.model.portfolio.GeographicAllocationSlice;
import io.github.brckjr.ppmp.domain.model.portfolio.SectorAllocationSlice;

import java.math.BigDecimal;
import java.util.List;

public record DashboardSummary(
        BigDecimal portfolioValue,
        BigDecimal portfolioValuePct,
        BigDecimal dailyPL,
        BigDecimal dailyPLPct,
        BigDecimal totalReturn,
        BigDecimal totalReturnPct,
        BigDecimal annualizedReturnPct,
        BigDecimal cashPosition,
        BigDecimal totalInvested,
        List<EquityPoint> equityCurve,
        List<AssetAllocationSlice> allocationByAssetClass,
        List<SectorAllocationSlice> allocationBySector,
        List<GeographicAllocationSlice> allocationByRegion
) {}
