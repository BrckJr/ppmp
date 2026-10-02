package io.github.brckjr.ppmp.app.instrument.mapper;

import io.github.brckjr.ppmp.api.instrument.dto.InstrumentDto;
import io.github.brckjr.ppmp.domain.model.instrument.Instrument;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface InstrumentDtoMapper {

  default Instrument toDomain(InstrumentDto dto) {
    if (dto == null) {
      return null;
    }
    return Instrument.create(
      dto.name(),
      dto.ticker(),
      dto.currency(),
      dto.isin(),
      dto.exchange(),
      dto.country(),
      dto.region(),
      dto.sector(),
      dto.type()
    );
  }

  default InstrumentDto toDto(Instrument domain) {
    return new InstrumentDto(
      domain.getId(),
      domain.getName().orElse(null),
      domain.getTicker().orElse(null),
      domain.getCurrency(),
      domain.getIsin().orElse(null),
      domain.getExchange().orElse(null),
      domain.getCountry().orElse(null),
      domain.getRegion().orElse(null),
      domain.getSector().orElse(null),
      domain.getType().orElse(null)
    );
  }
}
