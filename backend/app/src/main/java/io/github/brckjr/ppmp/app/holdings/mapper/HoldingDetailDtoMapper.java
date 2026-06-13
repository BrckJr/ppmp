package io.github.brckjr.ppmp.app.holdings.mapper;

import io.github.brckjr.ppmp.api.holdings.dto.HoldingDetailDto;
import io.github.brckjr.ppmp.domain.model.holding.HoldingDetail;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface HoldingDetailDtoMapper {

    HoldingDetailDto toDto(HoldingDetail source);

    HoldingDetail toDomain(HoldingDetailDto source);
}
