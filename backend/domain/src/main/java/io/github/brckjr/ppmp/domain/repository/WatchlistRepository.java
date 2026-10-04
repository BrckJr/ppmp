package io.github.brckjr.ppmp.domain.repository;


import io.github.brckjr.ppmp.domain.model.watchlist.Watchlist;

import java.util.List;

public interface WatchlistRepository extends BaseRepository<Watchlist> {

  List<Watchlist> findByUserId(java.util.UUID userId);
}
