package io.github.brckjr.ppmp.persistence.repository;

import io.github.brckjr.ppmp.domain.model.instrument.Instrument;
import io.github.brckjr.ppmp.domain.repository.InstrumentRepository;
import io.github.brckjr.ppmp.persistence.entity.InstrumentEntity;
import io.github.brckjr.ppmp.persistence.mapper.InstrumentMapper;
import jakarta.inject.Inject;

public class InstrumentRepositoryImpl extends BaseRepositoryImpl<Instrument, InstrumentEntity> implements InstrumentRepository {

    private final InstrumentMapper mapper;

    @Inject
    public InstrumentRepositoryImpl(InstrumentMapper mapper) {
        super(mapper, InstrumentEntity.class);
        this.mapper = mapper;
    }
}
