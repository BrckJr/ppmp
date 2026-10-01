package io.github.brckjr.ppmp.persistence.repository;

import io.github.brckjr.ppmp.domain.model.instrument.InstrumentPrice;
import io.github.brckjr.ppmp.domain.repository.InstrumentPriceRepository;
import io.github.brckjr.ppmp.persistence.entity.InstrumentPriceEntity;
import io.github.brckjr.ppmp.persistence.mapper.InstrumentPriceMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class InstrumentPriceRepositoryImpl extends BaseRepositoryImpl<InstrumentPrice, InstrumentPriceEntity> implements InstrumentPriceRepository {

    @Inject
    public InstrumentPriceRepositoryImpl(InstrumentPriceMapper mapper) {
        super(mapper, InstrumentPriceEntity.class);
    }
}
