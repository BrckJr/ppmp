package io.github.brckjr.ppmp.persistence.repository;

import io.github.brckjr.ppmp.domain.model.instrument.InstrumentPrice;
import io.github.brckjr.ppmp.domain.repository.InstrumentPriceRepository;
import io.github.brckjr.ppmp.persistence.entity.InstrumentPriceEntity;
import io.github.brckjr.ppmp.persistence.mapper.InstrumentPriceMapper;
import jakarta.inject.Inject;

public class InstrumentPriceRepositoryImpl extends BaseRepositoryImpl<InstrumentPrice, InstrumentPriceEntity> implements InstrumentPriceRepository {

    private final InstrumentPriceMapper mapper;

    @Inject
    public InstrumentPriceRepositoryImpl(InstrumentPriceMapper mapper) {
        super(mapper, InstrumentPriceEntity.class);
        this.mapper = mapper;
    }
}
