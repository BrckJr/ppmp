package io.github.brckjr.ppmp.app.risk;

import io.github.brckjr.ppmp.api.risk.RiskApi;
import io.github.brckjr.ppmp.api.risk.dto.RiskMetricsDto;
import io.github.brckjr.ppmp.app.risk.mapper.RiskMetricsDtoMapper;
import io.github.brckjr.ppmp.domain.service.risk.RiskService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.mapstruct.factory.Mappers;

@ApplicationScoped
public class RiskApp implements RiskApi {

    private final RiskService service;
    private final RiskMetricsDtoMapper mapper = Mappers.getMapper(RiskMetricsDtoMapper.class);

    @Inject
    public RiskApp(RiskService service) {
        this.service = service;
    }

    @Override
    public RiskMetricsDto getRiskMetrics() {
        return mapper.toDto(service.getRiskMetrics());
    }
}
