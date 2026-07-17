package io.github.brckjr.ppmp.app.dashboard.mapper;

import io.github.brckjr.ppmp.api.dashboard.dto.DashboardDto;
import io.github.brckjr.ppmp.app.shared.AllocationSliceDtoMapper;
import io.github.brckjr.ppmp.domain.model.dashboard.DashboardSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "cdi",
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    uses = {
        AllocationSliceDtoMapper.class // the others two are automatically by MapStruct
    }
)
public interface DashboardDtoMapper {

  @Mapping(source = "kpiMetrics", target = "kpis")
  @Mapping(source = "portfolioValueCurve", target = "portfolioValueCurve")
  @Mapping(source = "assetClassAllocations", target = "allocationByAssetClass")
  @Mapping(source = "sectorAllocations", target = "allocationBySector")
  @Mapping(source = "regionAllocations", target = "allocationByRegion")
  DashboardDto toDto(DashboardSummary source);
}