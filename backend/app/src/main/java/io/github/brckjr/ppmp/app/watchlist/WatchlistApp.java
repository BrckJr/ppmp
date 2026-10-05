package io.github.brckjr.ppmp.app.watchlist;

import io.github.brckjr.ppmp.api.watchlist.WatchlistApi;
import io.github.brckjr.ppmp.api.watchlist.dto.WatchlistDto;
import io.github.brckjr.ppmp.api.watchlist.dto.WatchlistItemDto;
import io.github.brckjr.ppmp.app.auth.CurrentUser;
import io.github.brckjr.ppmp.app.watchlist.mapper.WatchlistDtoMapper;
import io.github.brckjr.ppmp.app.watchlist.mapper.WatchlistItemDtoMapper;
import io.github.brckjr.ppmp.domain.service.watchlist.WatchlistService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@ApplicationScoped
public class WatchlistApp implements WatchlistApi {

    private final WatchlistService service;
    private final WatchlistDtoMapper watchlistMapper;
    private final WatchlistItemDtoMapper itemMapper;
    private final CurrentUser currentUser;

    @Inject
    public WatchlistApp(WatchlistService service, WatchlistDtoMapper watchlistMapper, WatchlistItemDtoMapper itemMapper, CurrentUser currentUser) {
        this.service = service;
        this.watchlistMapper = watchlistMapper;
        this.itemMapper = itemMapper;
        this.currentUser = currentUser;
    }

    @Override
    public List<WatchlistDto> getWatchlists() {
        return service.getWatchlists(currentUser.id()).stream().map(watchlistMapper::toDto).toList();
    }

    @Override
    public WatchlistDto createWatchlist(WatchlistDto newWatchlist) {
        try {
            return watchlistMapper.toDto(service.createWatchlist(currentUser.id(), newWatchlist.name(), newWatchlist.description()));
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteWatchlist(UUID watchlistId) {
        try {
            service.deleteWatchlist(currentUser.id(), watchlistId);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage(), ex);
        }
    }

    @Override
    public List<WatchlistItemDto> getWatchlistItems(UUID watchlistId) {
        try {
            return service.getItems(currentUser.id(), watchlistId).stream().map(itemMapper::toDto).toList();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage(), ex);
        }
    }

    @Override
    public WatchlistItemDto addWatchlistItem(UUID watchlistId, WatchlistItemDto newItem) {
        try {
            return itemMapper.toDto(service.addItem(currentUser.id(), watchlistId, newItem.instrumentId(), null, null));
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage(), ex);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(ex.getMessage(), ex);
        }
    }

    @Override
    public void removeWatchlistItem(UUID watchlistId, UUID itemId) {
        try {
            service.removeItem(currentUser.id(), watchlistId, itemId);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage(), ex);
        }
    }
}
