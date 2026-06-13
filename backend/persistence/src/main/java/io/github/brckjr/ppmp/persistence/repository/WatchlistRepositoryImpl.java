package io.github.brckjr.ppmp.persistence.repository;

import io.github.brckjr.ppmp.domain.model.watchlist.Watchlist;
import io.github.brckjr.ppmp.domain.repository.WatchlistRepository;
import io.github.brckjr.ppmp.persistence.entity.WatchlistEntity;
import io.github.brckjr.ppmp.persistence.mapper.WatchlistMapper;
import jakarta.inject.Inject;

public class WatchlistRepositoryImpl extends BaseRepositoryImpl<Watchlist, WatchlistEntity> implements WatchlistRepository {

    private final WatchlistMapper mapper;

    @Inject
    public WatchlistRepositoryImpl(WatchlistMapper mapper) {
        super(mapper, WatchlistEntity.class);
        this.mapper = mapper;
    }
}