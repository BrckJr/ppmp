package io.github.brckjr.ppmp.app.dashboard;

import io.github.brckjr.ppmp.api.dashboard.DashboardApi;
import io.github.brckjr.ppmp.api.dashboard.dto.DashboardDto;
import io.github.brckjr.ppmp.app.dashboard.mapper.DashboardDtoMapper;
import io.github.brckjr.ppmp.domain.service.dashboard.DashboardService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DashboardApp implements DashboardApi {
    @Inject
    DashboardService service;

    @Inject
    DashboardDtoMapper mapper;

    @Override
    public DashboardDto getDashboard(){

        return null;
    }

}
