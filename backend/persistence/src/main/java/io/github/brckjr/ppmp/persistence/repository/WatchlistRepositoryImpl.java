package io.github.brckjr.ppmp.persistence.repository;

import io.github.brckjr.ppmp.domain.model.watchlist.Watchlist;
import io.github.brckjr.ppmp.domain.repository.WatchlistRepository;
import io.github.brckjr.ppmp.persistence.entity.InstrumentEntity;
import io.github.brckjr.ppmp.persistence.entity.UserEntity;
import io.github.brckjr.ppmp.persistence.entity.WatchlistEntity;
import io.github.brckjr.ppmp.persistence.mapper.WatchlistMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class WatchlistRepositoryImpl extends BaseRepositoryImpl<Watchlist, WatchlistEntity> implements WatchlistRepository {

    @Inject
    public WatchlistRepositoryImpl(WatchlistMapper mapper) {
        super(mapper, WatchlistEntity.class);
    }

    @Override
    public List<Watchlist> findByUserId(UUID userId) {
        return jpaStreamer.stream(WatchlistEntity.class)
            .filter(entity -> entity.getUser().getId().equals(userId))
            .map(mapper::toModel)
            .toList();
    }

    @Override
    protected void resolveReferences(WatchlistEntity entity) {
        entity.setUser(entityManager.getReference(UserEntity.class, entity.getUser().getId()));
        entity.getItems().forEach(item -> {
            if (item.getInstrument() != null) {
                item.setInstrument(entityManager.getReference(InstrumentEntity.class, item.getInstrument().getId()));
            }
        });
    }
}
