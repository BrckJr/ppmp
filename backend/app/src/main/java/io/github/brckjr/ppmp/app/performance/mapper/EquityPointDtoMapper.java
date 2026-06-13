package io.github.brckjr.ppmp.app.performance.mapper;

import io.github.brckjr.ppmp.api.performance.dto.EquityPointDto;
import io.github.brckjr.ppmp.domain.model.performance.EquityPoint;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface EquityPointDtoMapper {

    EquityPointDto toDto(EquityPoint source);

    EquityPoint toDomain(EquityPointDto source);
}
