package io.github.brckjr.ppmp.api.risk;

import io.github.brckjr.ppmp.api.risk.dto.RiskMetricsDto;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/risk")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface RiskApi {

    @GET
    @Path("/")
    RiskMetricsDto getRiskMetrics();
}
