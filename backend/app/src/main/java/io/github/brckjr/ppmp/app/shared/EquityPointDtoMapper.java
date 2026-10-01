package io.github.brckjr.ppmp.app.shared;

import io.github.brckjr.ppmp.api.shared.EquityPointDto;
import io.github.brckjr.ppmp.domain.model.shared.EquityPoint;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface EquityPointDtoMapper {

  EquityPointDto toDto(EquityPoint source);

  EquityPoint toDomain(EquityPointDto source);
}
