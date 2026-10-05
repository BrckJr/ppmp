package io.github.brckjr.ppmp.app.dashboard;

import io.github.brckjr.ppmp.api.dashboard.DashboardApi;
import io.github.brckjr.ppmp.api.dashboard.dto.DashboardDto;
import io.github.brckjr.ppmp.app.auth.CurrentUser;
import io.github.brckjr.ppmp.app.dashboard.mapper.DashboardDtoMapper;
import io.github.brckjr.ppmp.domain.service.dashboard.DashboardService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DashboardApp implements DashboardApi {

  private final DashboardService service;
  private final DashboardDtoMapper mapper;
  private final CurrentUser currentUser;

  @Inject
  public DashboardApp(DashboardService service, DashboardDtoMapper mapper, CurrentUser currentUser) {
    this.service = service;
    this.mapper = mapper;
    this.currentUser = currentUser;
  }

  @Override
  public DashboardDto getDashboard() {
    return mapper.toDto(service.getDashboard(currentUser.id()));
  }
}
