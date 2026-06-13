package io.github.brckjr.ppmp.app.performance.mapper;

import io.github.brckjr.ppmp.api.performance.dto.DrawdownPointDto;
import io.github.brckjr.ppmp.domain.model.performance.DrawdownPoint;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DrawdownPointDtoMapper {

    DrawdownPointDto toDto(DrawdownPoint source);

    DrawdownPoint toDomain(DrawdownPointDto source);
}
