package io.github.brckjr.ppmp.domain.repository;


import io.github.brckjr.ppmp.domain.model.instrument.Instrument;

import java.util.Optional;

public interface InstrumentRepository extends BaseRepository<Instrument> {

  Optional<Instrument> findByTicker(String ticker);

  Optional<Instrument> findByIsin(String isin);
}
