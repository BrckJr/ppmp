package io.github.brckjr.ppmp.api.dashboard.dto;

import io.github.brckjr.ppmp.api.performance.dto.EquityPointDto;
import io.github.brckjr.ppmp.api.shared.AllocationSliceDto;

import java.util.List;

public record DashboardDto(
    KpiMetricsDto kpis,
    List<EquityPointDto> portfolioValueCurve,
    List<AllocationSliceDto> allocationByAssetClass,
    List<AllocationSliceDto> allocationBySector,
    List<AllocationSliceDto> allocationByRegion
) {
}