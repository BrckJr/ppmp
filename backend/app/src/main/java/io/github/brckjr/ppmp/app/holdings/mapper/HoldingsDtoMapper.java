package io.github.brckjr.ppmp.app.holdings.mapper;

import io.github.brckjr.ppmp.api.holdings.dto.HoldingsDto;
import io.github.brckjr.ppmp.domain.model.holding.Holdings;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = HoldingDetailDtoMapper.class)
public interface HoldingsDtoMapper {

    HoldingsDto toDto(Holdings source);

    Holdings toDomain(HoldingsDto source);
}
