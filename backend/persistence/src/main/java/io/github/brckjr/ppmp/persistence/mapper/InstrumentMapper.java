package io.github.brckjr.ppmp.persistence.mapper;

import io.github.brckjr.ppmp.domain.model.instrument.Instrument;
import io.github.brckjr.ppmp.persistence.entity.InstrumentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface InstrumentMapper extends BaseMapper<Instrument, InstrumentEntity> {

  @Override
  default Instrument toModel(InstrumentEntity entity) {
    if (entity == null) {
      return null;
    }
    return Instrument.reconstitute(
      entity.getId(),
      entity.getCreatedAt(),
      entity.getUpdatedAt(),
        entity.getName(),
        entity.getTicker(),
        entity.getCurrency(),
        entity.getIsin(),
        entity.getExchange(),
        entity.getCountry(),
        entity.getRegion(),
        entity.getSector(),
        entity.getType()
    );
  }

  @Override
  default InstrumentEntity toEntity(Instrument model) {
    if (model == null) {
      return null;
    }
    InstrumentEntity entity = new InstrumentEntity();
    entity.setId(model.getId());
    entity.setCreatedAt(model.getCreatedAt());
    entity.setUpdatedAt(model.getUpdatedAt());
    entity.setName(model.getName().orElse(null));
    entity.setTicker(model.getTicker().orElse(null));
    entity.setCurrency(model.getCurrency());
    entity.setIsin(model.getIsin().orElse(null));
    entity.setExchange(model.getExchange().orElse(null));
    entity.setCountry(model.getCountry().orElse(null));
    entity.setRegion(model.getRegion().orElse(null));
    entity.setSector(model.getSector().orElse(null));
    entity.setType(model.getType().orElse(null));
    return entity;
  }

  @Override
  default void updateEntityFromModel(Instrument model, @MappingTarget InstrumentEntity entity) {
    if (model == null || entity == null) {
      return;
    }
    entity.setUpdatedAt(model.getUpdatedAt());
    entity.setName(model.getName().orElse(null));
    entity.setTicker(model.getTicker().orElse(null));
    entity.setCurrency(model.getCurrency());
    entity.setIsin(model.getIsin().orElse(null));
    entity.setExchange(model.getExchange().orElse(null));
    entity.setCountry(model.getCountry().orElse(null));
    entity.setRegion(model.getRegion().orElse(null));
    entity.setSector(model.getSector().orElse(null));
    entity.setType(model.getType().orElse(null));
  }
}
