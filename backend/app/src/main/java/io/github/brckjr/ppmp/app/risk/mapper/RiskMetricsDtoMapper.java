package io.github.brckjr.ppmp.app.risk.mapper;

import io.github.brckjr.ppmp.api.risk.dto.RiskMetricsDto;
import io.github.brckjr.ppmp.domain.model.risk.RiskMetrics;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RiskMetricsDtoMapper {

    RiskMetricsDto toDto(RiskMetrics source);

    RiskMetrics toDomain(RiskMetricsDto source);
}
