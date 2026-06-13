package io.github.brckjr.ppmp.persistence.mapper;

import io.github.brckjr.ppmp.domain.model.instrument.InstrumentPrice;
import io.github.brckjr.ppmp.persistence.entity.InstrumentPriceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface InstrumentPriceMapper extends BaseMapper<InstrumentPrice, InstrumentPriceEntity> {

    private InstrumentMapper instrumentMapper() {
        return new InstrumentMapper() {
        };
    }

    @Override
    default InstrumentPrice toModel(InstrumentPriceEntity entity) {
        if (entity == null) {
            return null;
        }
        return InstrumentPrice.reconstitute(
                instrumentMapper().toModel(entity.getInstrument()),
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

    @Override
    default InstrumentPriceEntity toEntity(InstrumentPrice model) {
        if (model == null) {
            return null;
        }
        InstrumentPriceEntity entity = new InstrumentPriceEntity();
        entity.setId(model.getId());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setInstrument(instrumentMapper().toEntity(model.getInstrument()));
        entity.setOpen(model.getOpen());
        entity.setHigh(model.getHigh());
        entity.setLow(model.getLow());
        entity.setClose(model.getClose());
        entity.setAdjClose(model.getAdjClose().orElse(null));
        entity.setVolume(model.getVolume());
        entity.setCurrency(model.getCurrency());
        entity.setSource(model.getSource().orElse(null));
        return entity;
    }

    @Override
    default void updateEntityFromModel(InstrumentPrice model, @MappingTarget InstrumentPriceEntity entity) {
        if (model == null || entity == null) {
            return;
        }
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setInstrument(instrumentMapper().toEntity(model.getInstrument()));
        entity.setOpen(model.getOpen());
        entity.setHigh(model.getHigh());
        entity.setLow(model.getLow());
        entity.setClose(model.getClose());
        entity.setAdjClose(model.getAdjClose().orElse(null));
        entity.setVolume(model.getVolume());
        entity.setCurrency(model.getCurrency());
        entity.setSource(model.getSource().orElse(null));
    }
}
