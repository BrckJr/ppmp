package io.github.brckjr.ppmp.api.watchlist;

import io.github.brckjr.ppmp.api.watchlist.dto.WatchlistItemDto;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/watchlist")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface WatchlistApi {

    @GET
    @Path("/")
    List<WatchlistItemDto> getWatchlist();
}
