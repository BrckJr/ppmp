package io.github.brckjr.ppmp.api.instrument.dto;

import io.github.brckjr.ppmp.common.annotation.Required;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record InstrumentPriceDto(
  @Required UUID id,
  @Required UUID instrumentId,
  @Required LocalDate priceDate,
  @Required BigDecimal open,
  @Required BigDecimal high,
  @Required BigDecimal low,
  @Required BigDecimal close,
  BigDecimal adjClose,
  @Required Long volume,
  @Required String currency,
  String source
) {
}
