package io.github.brckjr.ppmp.api.performance.dto;

import java.util.List;

public record PerformanceDto(
        String benchmarkName,
        List<EquityPointDto> equityCurve,
        List<DrawdownPointDto> drawdownSeries,
        List<RollingVolPointDto> rollingVol
) {}