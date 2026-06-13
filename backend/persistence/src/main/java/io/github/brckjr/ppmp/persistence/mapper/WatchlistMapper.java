package io.github.brckjr.ppmp.persistence.mapper;

import io.github.brckjr.ppmp.domain.model.watchlist.Watchlist;
import io.github.brckjr.ppmp.domain.model.watchlist.WatchlistItem;
import io.github.brckjr.ppmp.persistence.entity.WatchlistEntity;
import io.github.brckjr.ppmp.persistence.entity.WatchlistItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "cdi")
public interface WatchlistMapper extends BaseMapper<Watchlist, WatchlistEntity> {

    private UserMapper userMapper() {
        return new UserMapper() {
        };
    }

    private WatchlistItemMapper watchlistItemMapper() {
        return new WatchlistItemMapper() {
        };
    }

    @Override
    default Watchlist toModel(WatchlistEntity entity) {
        if (entity == null) {
            return null;
        }
        return Watchlist.reconstitute(
                userMapper().toModel(entity.getUser()),
                entity.getName(),
                entity.getDescription(),
                toWatchlistItemModelList(entity.getItems())
        );
    }

    @Override
    default WatchlistEntity toEntity(Watchlist model) {
        if (model == null) {
            return null;
        }
        WatchlistEntity entity = new WatchlistEntity();
        entity.setId(model.getId());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setUser(userMapper().toEntity(model.getUser()));
        entity.setName(model.getName());
        entity.setDescription(model.getDescription().orElse(null));

        List<WatchlistItemEntity> items = toWatchlistItemEntityList(model.getItems());
        entity.setItems(items);
        if (items != null) {
            items.forEach(item -> item.setWatchlist(entity));
        }
        return entity;
    }

    @Override
    default void updateEntityFromModel(Watchlist model, @MappingTarget WatchlistEntity entity) {
        if (model == null || entity == null) {
            return;
        }
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setUser(userMapper().toEntity(model.getUser()));
        entity.setName(model.getName());
        entity.setDescription(model.getDescription().orElse(null));

        List<WatchlistItemEntity> items = toWatchlistItemEntityList(model.getItems());
        entity.setItems(items);
        if (items != null) {
            items.forEach(item -> item.setWatchlist(entity));
        }
    }

    default List<WatchlistItem> toWatchlistItemModelList(List<WatchlistItemEntity> entities) {
        if (entities == null) {
            return new ArrayList<>();
        }
        List<WatchlistItem> items = new ArrayList<>(entities.size());
        for (WatchlistItemEntity entity : entities) {
            items.add(watchlistItemMapper().toModel(entity));
        }
        return items;
    }

    default List<WatchlistItemEntity> toWatchlistItemEntityList(List<WatchlistItem> domains) {
        if (domains == null) {
            return new ArrayList<>();
        }
        List<WatchlistItemEntity> items = new ArrayList<>(domains.size());
        for (WatchlistItem domain : domains) {
            items.add(watchlistItemMapper().toEntity(domain));
        }
        return items;
    }
}
