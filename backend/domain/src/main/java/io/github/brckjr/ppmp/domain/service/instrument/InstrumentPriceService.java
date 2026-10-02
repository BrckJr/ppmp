package io.github.brckjr.ppmp.domain.service.instrument;

import io.github.brckjr.ppmp.domain.model.instrument.InstrumentPrice;
import io.github.brckjr.ppmp.domain.repository.InstrumentPriceRepository;
import io.github.brckjr.ppmp.domain.repository.InstrumentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class InstrumentPriceService {

  private final InstrumentPriceRepository priceRepository;
  private final InstrumentRepository instrumentRepository;

  @Inject
  public InstrumentPriceService(InstrumentPriceRepository priceRepository, InstrumentRepository instrumentRepository) {
    this.priceRepository = priceRepository;
    this.instrumentRepository = instrumentRepository;
  }

  /** Returns empty if the instrument is unknown, otherwise its prices (oldest first). */
  public Optional<List<InstrumentPrice>> getPrices(UUID instrumentId, LocalDate from, LocalDate to) {
    Objects.requireNonNull(instrumentId, "Instrument id cannot be null");
    if (from != null && to != null && from.isAfter(to)) {
      throw new IllegalArgumentException("'from' must not be after 'to'");
    }
    if (instrumentRepository.findById(instrumentId).isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(priceRepository.findByInstrumentId(instrumentId, from, to));
  }

  public Optional<InstrumentPrice> getLatestPrice(UUID instrumentId) {
    Objects.requireNonNull(instrumentId, "Instrument id cannot be null");
    return priceRepository.findLatestByInstrumentId(instrumentId);
  }

  /** Stores a price; at most one price per instrument and day is allowed. */
  public InstrumentPrice createPrice(InstrumentPrice price) {
    Objects.requireNonNull(price, "Price cannot be null");
    UUID instrumentId = price.getInstrument().getId();
    if (instrumentRepository.findById(instrumentId).isEmpty()) {
      throw new IllegalArgumentException("Unknown instrument: " + instrumentId);
    }
    if (priceRepository.findByInstrumentIdAndDate(instrumentId, price.getPriceDate()).isPresent()) {
      throw new IllegalArgumentException("Price already exists for instrument " + instrumentId + " on " + price.getPriceDate());
    }
    return priceRepository.persist(price);
  }
}
