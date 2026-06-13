package io.github.brckjr.ppmp.api.dashboard;

import io.github.brckjr.ppmp.api.dashboard.dto.DashboardDto;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/dashboard")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface DashboardApi {

    @GET
    @Path("/")
    DashboardDto getDashboard();

}
