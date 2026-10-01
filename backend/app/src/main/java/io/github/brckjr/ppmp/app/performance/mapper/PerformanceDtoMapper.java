package io.github.brckjr.ppmp.app.performance.mapper;

import io.github.brckjr.ppmp.api.performance.dto.PerformanceDto;
import io.github.brckjr.ppmp.app.shared.EquityPointDtoMapper;
import io.github.brckjr.ppmp.domain.model.performance.PerformanceMetrics;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "cdi",
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    uses = {EquityPointDtoMapper.class, DrawdownPointDtoMapper.class, RollingVolPointDtoMapper.class}
)
public interface PerformanceDtoMapper {

  PerformanceDto toDto(PerformanceMetrics source);

  PerformanceMetrics toDomain(PerformanceDto source);
}
