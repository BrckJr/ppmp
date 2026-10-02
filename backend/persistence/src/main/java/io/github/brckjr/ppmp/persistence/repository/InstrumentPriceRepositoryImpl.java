package io.github.brckjr.ppmp.persistence.repository;

import io.github.brckjr.ppmp.domain.model.instrument.InstrumentPrice;
import io.github.brckjr.ppmp.domain.repository.InstrumentPriceRepository;
import io.github.brckjr.ppmp.persistence.entity.InstrumentEntity;
import io.github.brckjr.ppmp.persistence.entity.InstrumentPriceEntity;
import io.github.brckjr.ppmp.persistence.mapper.InstrumentPriceMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class InstrumentPriceRepositoryImpl extends BaseRepositoryImpl<InstrumentPrice, InstrumentPriceEntity> implements InstrumentPriceRepository {

    @Inject
    public InstrumentPriceRepositoryImpl(InstrumentPriceMapper mapper) {
        super(mapper, InstrumentPriceEntity.class);
    }

    @Override
    public List<InstrumentPrice> findByInstrumentId(UUID instrumentId, LocalDate from, LocalDate to) {
        return jpaStreamer.stream(InstrumentPriceEntity.class)
            .filter(entity -> entity.getInstrument().getId().equals(instrumentId))
            .filter(entity -> from == null || !entity.getPriceDate().isBefore(from))
            .filter(entity -> to == null || !entity.getPriceDate().isAfter(to))
            .sorted(Comparator.comparing(InstrumentPriceEntity::getPriceDate))
            .map(mapper::toModel)
            .toList();
    }

    @Override
    public Optional<InstrumentPrice> findLatestByInstrumentId(UUID instrumentId) {
        return jpaStreamer.stream(InstrumentPriceEntity.class)
            .filter(entity -> entity.getInstrument().getId().equals(instrumentId))
            .max(Comparator.comparing(InstrumentPriceEntity::getPriceDate))
            .map(mapper::toModel);
    }

    @Override
    public Optional<InstrumentPrice> findByInstrumentIdAndDate(UUID instrumentId, LocalDate priceDate) {
        return jpaStreamer.stream(InstrumentPriceEntity.class)
            .filter(entity -> entity.getInstrument().getId().equals(instrumentId))
            .filter(entity -> entity.getPriceDate().equals(priceDate))
            .findFirst()
            .map(mapper::toModel);
    }

    @Override
    protected void resolveReferences(InstrumentPriceEntity entity) {
        InstrumentEntity instrument = entity.getInstrument();
        entity.setInstrument(entityManager.getReference(InstrumentEntity.class, instrument.getId()));
    }
}
