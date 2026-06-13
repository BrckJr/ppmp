package io.github.brckjr.ppmp.app.dashboard;

import io.github.brckjr.ppmp.api.dashboard.DashboardApi;
import io.github.brckjr.ppmp.api.dashboard.dto.DashboardDto;
import io.github.brckjr.ppmp.app.dashboard.mapper.DashboardDtoMapper;
import io.github.brckjr.ppmp.domain.service.dashboard.DashboardService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.mapstruct.factory.Mappers;

@ApplicationScoped
public class DashboardApp implements DashboardApi {

    private final DashboardService service;
    private final DashboardDtoMapper mapper = Mappers.getMapper(DashboardDtoMapper.class);

    @Inject
    public DashboardApp(DashboardService service) {
        this.service = service;
    }

    @Override
    public DashboardDto getDashboard() {
        return mapper.toDto(service.getDashboard());
    }

}
