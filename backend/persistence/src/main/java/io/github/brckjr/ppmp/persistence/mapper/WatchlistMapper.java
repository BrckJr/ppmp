package io.github.brckjr.ppmp.persistence.mapper;

import io.github.brckjr.ppmp.domain.model.shared.User;
import io.github.brckjr.ppmp.domain.model.watchlist.Watchlist;
import io.github.brckjr.ppmp.domain.model.watchlist.WatchlistItem;
import io.github.brckjr.ppmp.persistence.entity.UserEntity;
import io.github.brckjr.ppmp.persistence.entity.WatchlistEntity;
import io.github.brckjr.ppmp.persistence.entity.WatchlistItemEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {InstrumentMapper.class})
public interface WatchlistMapper extends BaseMapper<Watchlist, WatchlistEntity> {

    User mapUserEntityToModel(UserEntity entity);

    @ObjectFactory
    default Watchlist createModel(WatchlistEntity entity) {
        return Watchlist.reconstitute(
                mapUserEntityToModel(entity.getUser()),
                entity.getName(),
                entity.getDescription(),
                toWatchlistItemModelList(entity.getItems()) // MapStruct knows how to map lists
        );
    }

    List<WatchlistItem> toWatchlistItemModelList(List<WatchlistItemEntity> entities);
    List<WatchlistItemEntity> toWatchlistItemEntityList(List<WatchlistItem> domains);

    WatchlistItem toItemModel(WatchlistItemEntity entity);

    @Mapping(target = "watchlist", ignore = true)
    WatchlistItemEntity toItemEntity(WatchlistItem item);

    @AfterMapping
    default void establishBidirectionalRelationships(@MappingTarget WatchlistEntity watchlistEntity) {
        if (watchlistEntity.getItems() != null) {
            watchlistEntity.getItems().forEach(item -> item.setWatchlist(watchlistEntity));
        }
    }
}
