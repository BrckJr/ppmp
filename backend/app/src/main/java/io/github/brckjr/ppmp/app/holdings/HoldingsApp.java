package io.github.brckjr.ppmp.app.holdings;

import io.github.brckjr.ppmp.api.holdings.HoldingsApi;
import io.github.brckjr.ppmp.api.holdings.dto.HoldingDetailDto;
import io.github.brckjr.ppmp.api.holdings.dto.HoldingsDto;
import io.github.brckjr.ppmp.app.holdings.mapper.HoldingDetailDtoMapper;
import io.github.brckjr.ppmp.app.holdings.mapper.HoldingsDtoMapper;
import io.github.brckjr.ppmp.domain.service.portfolio.PortfolioService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.PathParam;

import java.util.UUID;

@ApplicationScoped
public class HoldingsApp implements HoldingsApi {

  private final PortfolioService service;
  private final HoldingsDtoMapper holdingsMapper;
  private final HoldingDetailDtoMapper holdingDetailMapper;

  @Inject
  public HoldingsApp(PortfolioService service, HoldingsDtoMapper holdingsMapper, HoldingDetailDtoMapper holdingDetailMapper) {
    this.service = service;
    this.holdingsMapper = holdingsMapper;
    this.holdingDetailMapper = holdingDetailMapper;
  }

  @Override
  public HoldingsDto getHoldings() {
    return holdingsMapper.toDto(service.getHoldings());
  }

  @Override
  public HoldingDetailDto getHolding(@PathParam("id") UUID id) {
    return holdingDetailMapper.toDto(service.getHolding(id));
  }
}
