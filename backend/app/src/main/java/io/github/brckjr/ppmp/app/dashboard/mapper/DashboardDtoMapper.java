package io.github.brckjr.ppmp.app.dashboard.mapper;

import io.github.brckjr.ppmp.api.dashboard.dto.DashboardDto;
import io.github.brckjr.ppmp.domain.model.dashboard.DashboardSummary;
import io.github.brckjr.ppmp.app.performance.mapper.EquityPointDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "cdi",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {AllocationSliceDtoMapper.class, EquityPointDtoMapper.class}
)
public interface DashboardDtoMapper {

    DashboardDto toDto(DashboardSummary source);

    DashboardSummary toDomain(DashboardDto source);
}
