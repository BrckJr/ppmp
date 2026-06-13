package io.github.brckjr.ppmp.api.performance;

import io.github.brckjr.ppmp.api.performance.dto.PerformanceDto;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/performance")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface PerformanceApi {

    @GET
    @Path("/")
    PerformanceDto getPerformance();
}
