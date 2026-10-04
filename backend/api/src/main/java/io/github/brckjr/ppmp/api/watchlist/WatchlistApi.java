package io.github.brckjr.ppmp.api.watchlist;

import io.github.brckjr.ppmp.api.watchlist.dto.WatchlistDto;
import io.github.brckjr.ppmp.api.watchlist.dto.WatchlistItemDto;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@Path("/watchlists")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Watchlist", description = "Watchlists and the instruments on them")
public interface WatchlistApi {

    @GET
    @Path("")
    @Operation(summary = "List watchlists", description = "Lists all watchlists; the 'default watchlist' is created on first access")
    @APIResponse(responseCode = "200", description = "Success")
    List<WatchlistDto> getWatchlists();

    @POST
    @Path("")
    @Operation(summary = "Create watchlist", description = "Creates a new watchlist with a unique name and an optional description")
    @APIResponse(responseCode = "200", description = "Created successfully", content = @Content(schema = @Schema(implementation = WatchlistDto.class)))
    @APIResponse(responseCode = "400", description = "Invalid payload or name already in use")
    WatchlistDto createWatchlist(@Valid WatchlistDto newWatchlist);

    @GET
    @Path("/{watchlistId}/items")
    @Operation(summary = "List watchlist items", description = "Lists the instruments on a watchlist")
    @APIResponse(responseCode = "200", description = "Success")
    @APIResponse(responseCode = "404", description = "Watchlist not found")
    List<WatchlistItemDto> getWatchlistItems(@PathParam("watchlistId") UUID watchlistId);

    @POST
    @Path("/{watchlistId}/items")
    @Operation(summary = "Add instrument to watchlist", description = "Adds an already known instrument (referenced by instrumentId) to the watchlist")
    @APIResponse(responseCode = "200", description = "Added successfully", content = @Content(schema = @Schema(implementation = WatchlistItemDto.class)))
    @APIResponse(responseCode = "400", description = "Unknown instrument or instrument already on the watchlist")
    @APIResponse(responseCode = "404", description = "Watchlist not found")
    WatchlistItemDto addWatchlistItem(@PathParam("watchlistId") UUID watchlistId, @Valid WatchlistItemDto newItem);

    @DELETE
    @Path("/{watchlistId}/items/{itemId}")
    @Operation(summary = "Remove instrument from watchlist")
    @APIResponse(responseCode = "204", description = "Removed successfully")
    @APIResponse(responseCode = "404", description = "Watchlist or item not found")
    void removeWatchlistItem(@PathParam("watchlistId") UUID watchlistId, @PathParam("itemId") UUID itemId);
}
