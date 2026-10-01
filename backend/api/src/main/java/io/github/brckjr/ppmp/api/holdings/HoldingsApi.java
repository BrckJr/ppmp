package io.github.brckjr.ppmp.api.holdings;

import io.github.brckjr.ppmp.api.holdings.dto.HoldingDetailDto;
import io.github.brckjr.ppmp.api.holdings.dto.HoldingsDto;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/holdings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface HoldingsApi {

    @GET
    @Path("/")
    HoldingsDto getHoldings();

    @GET
    @Path("/{ticker}")
    HoldingDetailDto getHolding(@PathParam("ticker") String ticker);
}
