package io.github.brckjr.ppmp.domain.model.dashboard;

import io.github.brckjr.ppmp.domain.model.shared.EquityPoint;
import io.github.brckjr.ppmp.domain.model.shared.allocation.AssetAllocationSlice;
import io.github.brckjr.ppmp.domain.model.shared.allocation.GeographicAllocationSlice;
import io.github.brckjr.ppmp.domain.model.shared.allocation.SectorAllocationSlice;

import java.util.List;

public record DashboardSummary(
    KpiMetrics kpiMetrics,
    List<EquityPoint> portfolioValueCurve,
    List<AssetAllocationSlice> assetClassAllocations,
    List<SectorAllocationSlice> sectorAllocations,
    List<GeographicAllocationSlice> regionAllocations
) {
}
