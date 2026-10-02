package io.github.brckjr.ppmp.domain.repository;


import io.github.brckjr.ppmp.domain.model.instrument.InstrumentPrice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InstrumentPriceRepository extends BaseRepository<InstrumentPrice> {

  /** Prices of one instrument within the inclusive date range (null bounds are open), oldest first. */
  List<InstrumentPrice> findByInstrumentId(UUID instrumentId, LocalDate from, LocalDate to);

  Optional<InstrumentPrice> findLatestByInstrumentId(UUID instrumentId);

  Optional<InstrumentPrice> findByInstrumentIdAndDate(UUID instrumentId, LocalDate priceDate);
}
