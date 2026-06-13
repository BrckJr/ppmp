package io.github.brckjr.ppmp.app.watchlist.mapper;

import io.github.brckjr.ppmp.api.watchlist.dto.WatchlistItemDto;
import io.github.brckjr.ppmp.domain.model.watchlist.WatchlistItemView;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface WatchlistItemDtoMapper {

    WatchlistItemDto toDto(WatchlistItemView source);

    WatchlistItemView toDomain(WatchlistItemDto source);
}
