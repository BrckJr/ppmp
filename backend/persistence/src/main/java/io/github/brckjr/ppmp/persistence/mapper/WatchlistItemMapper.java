package io.github.brckjr.ppmp.persistence.mapper;

import io.github.brckjr.ppmp.domain.model.instrument.Instrument;
import io.github.brckjr.ppmp.domain.model.watchlist.WatchlistItem;
import io.github.brckjr.ppmp.persistence.entity.InstrumentEntity;
import io.github.brckjr.ppmp.persistence.entity.WatchlistItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;

@Mapper(componentModel = "jakarta", uses = {InstrumentMapper.class})
public interface WatchlistItemMapper extends BaseMapper<WatchlistItem, WatchlistItemEntity> {

    // Explicit bridge so MapStruct delegates Instrument conversions to InstrumentMapper
    Instrument mapInstrumentEntityToDomain(InstrumentEntity entity);

    @ObjectFactory
    default WatchlistItem createDomain(WatchlistItemEntity entity) {
        return WatchlistItem.reconstitute(
                mapInstrumentEntityToDomain(entity.getInstrument()),
                entity.getNotes(),
                entity.getPriority()
        );
    }

    @Override
    @Mapping(target = "watchlist", ignore = true)
        // Handled upstream by WatchlistMapper's @AfterMapping lifecycle hook
    WatchlistItemEntity toEntity(WatchlistItem domain);
}
