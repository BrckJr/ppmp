package io.github.brckjr.ppmp.persistence.mapper;

import io.github.brckjr.ppmp.domain.model.instrument.Instrument;
import io.github.brckjr.ppmp.domain.model.instrument.InstrumentPrice;
import io.github.brckjr.ppmp.persistence.entity.InstrumentEntity;
import io.github.brckjr.ppmp.persistence.entity.InstrumentPriceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;
import org.mapstruct.ReportingPolicy;

import java.util.Optional;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {InstrumentMapper.class})
public interface InstrumentPriceMapper extends BaseMapper<InstrumentPrice, InstrumentPriceEntity> {

    Instrument mapInstrumentEntityToModel(InstrumentEntity entity);

    @ObjectFactory
    default InstrumentPrice createDomain(InstrumentPriceEntity entity) {

        return InstrumentPrice.reconstitute(
                mapInstrumentEntityToModel(entity.getInstrument()),
                entity.getOpen(),
                entity.getHigh(),
                entity.getLow(),
                entity.getClose(),
                entity.getAdjClose(),
                entity.getVolume(),
                entity.getCurrency(),
                entity.getSource()
        );
    }

    default <T> T unwrapOptional(Optional<T> optional) {
        return optional.orElse(null);
    }
}
