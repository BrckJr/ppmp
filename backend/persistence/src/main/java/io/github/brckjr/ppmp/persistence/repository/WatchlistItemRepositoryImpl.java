package io.github.brckjr.ppmp.persistence.repository;

import io.github.brckjr.ppmp.domain.model.watchlist.WatchlistItem;
import io.github.brckjr.ppmp.domain.repository.WatchlistItemRepository;
import io.github.brckjr.ppmp.persistence.entity.WatchlistItemEntity;
import io.github.brckjr.ppmp.persistence.mapper.WatchlistItemMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class WatchlistItemRepositoryImpl extends BaseRepositoryImpl<WatchlistItem, WatchlistItemEntity> implements WatchlistItemRepository {

    @Inject
    public WatchlistItemRepositoryImpl(WatchlistItemMapper mapper) {
        super(mapper, WatchlistItemEntity.class);
    }
}
