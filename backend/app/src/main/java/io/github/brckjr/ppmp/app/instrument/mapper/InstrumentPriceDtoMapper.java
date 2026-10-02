package io.github.brckjr.ppmp.app.instrument.mapper;

import io.github.brckjr.ppmp.api.instrument.dto.InstrumentPriceDto;
import io.github.brckjr.ppmp.domain.model.instrument.InstrumentPrice;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface InstrumentPriceDtoMapper {

  default InstrumentPriceDto toDto(InstrumentPrice domain) {
    return new InstrumentPriceDto(
      domain.getId(),
      domain.getInstrument().getId(),
      domain.getPriceDate(),
      domain.getOpen(),
      domain.getHigh(),
      domain.getLow(),
      domain.getClose(),
      domain.getAdjClose().orElse(null),
      domain.getVolume(),
      domain.getCurrency(),
      domain.getSource().orElse(null)
    );
  }
}
