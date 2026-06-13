package io.github.brckjr.ppmp.app.performance.mapper;

import io.github.brckjr.ppmp.api.performance.dto.RollingVolPointDto;
import io.github.brckjr.ppmp.domain.model.performance.RollingVolPoint;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RollingVolPointDtoMapper {

    RollingVolPointDto toDto(RollingVolPoint source);

    RollingVolPoint toDomain(RollingVolPointDto source);
}
