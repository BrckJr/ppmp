package io.github.brckjr.ppmp.app.watchlist.mapper;

import io.github.brckjr.ppmp.api.watchlist.dto.WatchlistDto;
import io.github.brckjr.ppmp.domain.model.watchlist.Watchlist;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface WatchlistDtoMapper {

    default WatchlistDto toDto(Watchlist domain) {
        return new WatchlistDto(
            domain.getId(),
            domain.getName(),
            domain.getDescription().orElse(null),
            domain.getItems().size()
        );
    }
}
