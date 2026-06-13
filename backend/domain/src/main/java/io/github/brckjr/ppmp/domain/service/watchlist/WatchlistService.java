package io.github.brckjr.ppmp.domain.service.watchlist;

import io.github.brckjr.ppmp.domain.enums.AnalystRating;
import io.github.brckjr.ppmp.domain.model.watchlist.WatchlistItemView;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class WatchlistService {

    public List<WatchlistItemView> getWatchlist() {
        return List.of(
                new WatchlistItemView(
                        "UNKNOWN",
                        "Unknown",
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        AnalystRating.HOLD,
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        BigDecimal.ZERO
                )
        );
    }
}
