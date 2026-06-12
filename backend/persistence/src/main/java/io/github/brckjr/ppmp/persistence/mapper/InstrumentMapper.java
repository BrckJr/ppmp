package io.github.brckjr.ppmp.persistence.mapper;

import io.github.brckjr.ppmp.domain.model.instrument.Instrument;
import io.github.brckjr.ppmp.persistence.entity.InstrumentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface InstrumentMapper extends BaseMapper<Instrument, InstrumentEntity>{

    @ObjectFactory
    default Instrument createModel(InstrumentEntity entity) {
        return Instrument.reconstitute(
                entity.getType(),
                entity.getName(),
                entity.getTicker(),
                entity.getCurrency(),
                entity.getIsin(),
                entity.getExchange(),
                entity.getCountry(),
                entity.getRegion(),
                entity.getSector()
        );
    }
}
