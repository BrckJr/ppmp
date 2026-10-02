package io.github.brckjr.ppmp.persistence.repository;

import io.github.brckjr.ppmp.domain.model.instrument.Instrument;
import io.github.brckjr.ppmp.domain.repository.InstrumentRepository;
import io.github.brckjr.ppmp.persistence.entity.InstrumentEntity;
import io.github.brckjr.ppmp.persistence.mapper.InstrumentMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class InstrumentRepositoryImpl extends BaseRepositoryImpl<Instrument, InstrumentEntity> implements InstrumentRepository {

    @Inject
    public InstrumentRepositoryImpl(InstrumentMapper mapper) {
        super(mapper, InstrumentEntity.class);
    }

    @Override
    public Optional<Instrument> findByTicker(String ticker) {
        if (ticker == null) {
            return Optional.empty();
        }
        return jpaStreamer.stream(InstrumentEntity.class)
            .filter(entity -> entity.getTicker() != null && entity.getTicker().equalsIgnoreCase(ticker))
            .findFirst()
            .map(mapper::toModel);
    }

    @Override
    public Optional<Instrument> findByIsin(String isin) {
        if (isin == null) {
            return Optional.empty();
        }
        return jpaStreamer.stream(InstrumentEntity.class)
            .filter(entity -> entity.getIsin() != null && entity.getIsin().equalsIgnoreCase(isin))
            .findFirst()
            .map(mapper::toModel);
    }
}
