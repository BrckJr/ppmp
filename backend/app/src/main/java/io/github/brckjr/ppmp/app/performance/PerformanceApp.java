package io.github.brckjr.ppmp.app.performance;

import io.github.brckjr.ppmp.api.performance.PerformanceApi;
import io.github.brckjr.ppmp.api.performance.dto.PerformanceDto;
import io.github.brckjr.ppmp.app.performance.mapper.PerformanceDtoMapper;
import io.github.brckjr.ppmp.domain.service.performance.PerformanceService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.mapstruct.factory.Mappers;

@ApplicationScoped
public class PerformanceApp implements PerformanceApi {

    private final PerformanceService service;
    private final PerformanceDtoMapper mapper = Mappers.getMapper(PerformanceDtoMapper.class);

    @Inject
    public PerformanceApp(PerformanceService service) {
        this.service = service;
    }

    @Override
    public PerformanceDto getPerformance() {
        return mapper.toDto(service.getPerformance());
    }
}
