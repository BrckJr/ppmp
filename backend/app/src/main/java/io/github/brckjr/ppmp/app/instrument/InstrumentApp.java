package io.github.brckjr.ppmp.app.instrument;

import io.github.brckjr.ppmp.api.instrument.InstrumentApi;
import io.github.brckjr.ppmp.api.instrument.dto.InstrumentDto;
import io.github.brckjr.ppmp.api.instrument.dto.InstrumentPriceDto;
import io.github.brckjr.ppmp.app.instrument.mapper.InstrumentDtoMapper;
import io.github.brckjr.ppmp.app.instrument.mapper.InstrumentPriceDtoMapper;
import io.github.brckjr.ppmp.domain.service.instrument.InstrumentPriceService;
import io.github.brckjr.ppmp.domain.service.instrument.InstrumentService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class InstrumentApp implements InstrumentApi {

  private final InstrumentService instrumentService;
  private final InstrumentPriceService priceService;
  private final InstrumentDtoMapper instrumentDtoMapper;
  private final InstrumentPriceDtoMapper priceDtoMapper;

  @Inject
  public InstrumentApp(
    InstrumentService instrumentService,
    InstrumentPriceService priceService,
    InstrumentDtoMapper instrumentDtoMapper,
    InstrumentPriceDtoMapper priceDtoMapper
  ) {
    this.instrumentService = instrumentService;
    this.priceService = priceService;
    this.instrumentDtoMapper = instrumentDtoMapper;
    this.priceDtoMapper = priceDtoMapper;
  }

  @Override
  public List<InstrumentDto> getAllInstruments(String query, String type, int limit, int offset) {
    return instrumentService.getAllInstruments(query, type, limit, offset).stream().map(instrumentDtoMapper::toDto).toList();
  }

  @Override
  public InstrumentDto getInstrumentById(UUID id) {
    return instrumentService.getInstrumentById(id)
      .map(instrumentDtoMapper::toDto)
      .orElseThrow(() -> new NotFoundException("Instrument not found: " + id));
  }

  @Override
  public InstrumentDto createInstrument(InstrumentDto newInstrument) {
    try {
      return instrumentDtoMapper.toDto(instrumentService.createInstrument(instrumentDtoMapper.toDomain(newInstrument)));
    } catch (IllegalArgumentException | NullPointerException ex) {
      throw new BadRequestException(ex.getMessage(), ex);
    }
  }

  @Override
  public List<InstrumentPriceDto> getInstrumentPrices(UUID id, LocalDate from, LocalDate to) {
    try {
      return priceService.getPrices(id, from, to)
        .orElseThrow(() -> new NotFoundException("Instrument not found: " + id))
        .stream()
        .map(priceDtoMapper::toDto)
        .toList();
    } catch (IllegalArgumentException ex) {
      throw new BadRequestException(ex.getMessage(), ex);
    }
  }

  @Override
  public InstrumentPriceDto getLatestInstrumentPrice(UUID id) {
    return priceService.getLatestPrice(id)
      .map(priceDtoMapper::toDto)
      .orElseThrow(() -> new NotFoundException("No price found for instrument: " + id));
  }
}
