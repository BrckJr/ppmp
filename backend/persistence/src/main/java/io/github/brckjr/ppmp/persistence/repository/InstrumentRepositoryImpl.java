package io.github.brckjr.ppmp.persistence.repository;

import io.github.brckjr.ppmp.domain.model.instrument.Instrument;
import io.github.brckjr.ppmp.domain.repository.InstrumentRepository;
import io.github.brckjr.ppmp.persistence.entity.InstrumentEntity;
import io.github.brckjr.ppmp.persistence.mapper.InstrumentMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class InstrumentRepositoryImpl extends BaseRepositoryImpl<Instrument, InstrumentEntity> implements InstrumentRepository {

    @Inject
    public InstrumentRepositoryImpl(InstrumentMapper mapper) {
        super(mapper, InstrumentEntity.class);
    }
}
