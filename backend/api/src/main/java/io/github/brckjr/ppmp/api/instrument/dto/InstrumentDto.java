package io.github.brckjr.ppmp.api.instrument.dto;

import io.github.brckjr.ppmp.common.annotation.Required;

import java.util.UUID;

public record InstrumentDto(
  UUID id,
  String name,
  @Required String ticker,
  @Required String currency,
  String isin,
  String exchange,
  String country,
  String region,
  String sector,
  @Required String type
) {
}
