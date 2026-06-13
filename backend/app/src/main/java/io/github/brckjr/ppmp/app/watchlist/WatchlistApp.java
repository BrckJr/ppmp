package io.github.brckjr.ppmp.app.watchlist;

import io.github.brckjr.ppmp.api.watchlist.WatchlistApi;
import io.github.brckjr.ppmp.api.watchlist.dto.WatchlistItemDto;
import io.github.brckjr.ppmp.app.watchlist.mapper.WatchlistItemDtoMapper;
import io.github.brckjr.ppmp.domain.service.watchlist.WatchlistService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.mapstruct.factory.Mappers;

import java.util.List;

@ApplicationScoped
public class WatchlistApp implements WatchlistApi {

    private final WatchlistService service;
    private final WatchlistItemDtoMapper mapper = Mappers.getMapper(WatchlistItemDtoMapper.class);

    @Inject
    public WatchlistApp(WatchlistService service) {
        this.service = service;
    }

    @Override
    public List<WatchlistItemDto> getWatchlist() {
        return service.getWatchlist().stream().map(mapper::toDto).toList();
    }
}
