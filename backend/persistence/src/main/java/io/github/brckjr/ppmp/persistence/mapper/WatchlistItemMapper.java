package io.github.brckjr.ppmp.persistence.mapper;

import io.github.brckjr.ppmp.domain.model.watchlist.WatchlistItem;
import io.github.brckjr.ppmp.persistence.entity.WatchlistItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface WatchlistItemMapper extends BaseMapper<WatchlistItem, WatchlistItemEntity> {

    private InstrumentMapper instrumentMapper() {
        return new InstrumentMapper() {
        };
    }

    @Override
    default WatchlistItem toModel(WatchlistItemEntity entity) {
        if (entity == null) {
            return null;
        }
        return WatchlistItem.reconstitute(
                entity.getId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                instrumentMapper().toModel(entity.getInstrument()),
                entity.getNotes(),
                entity.getPriority()
        );
    }

    @Override
    @Mapping(target = "watchlist", ignore = true)
    default WatchlistItemEntity toEntity(WatchlistItem domain) {
        if (domain == null) {
            return null;
        }
        WatchlistItemEntity entity = new WatchlistItemEntity();
        entity.setId(domain.getId());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setInstrument(instrumentMapper().toEntity(domain.getInstrument().orElse(null)));
        entity.setNotes(domain.getNotes().orElse(null));
        entity.setPriority(domain.getPriority().orElse(null));
        return entity;
    }

    @Override
    default void updateEntityFromModel(WatchlistItem domain, @MappingTarget WatchlistItemEntity entity) {
        if (domain == null || entity == null) {
            return;
        }
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setInstrument(instrumentMapper().toEntity(domain.getInstrument().orElse(null)));
        entity.setNotes(domain.getNotes().orElse(null));
        entity.setPriority(domain.getPriority().orElse(null));
    }
}
