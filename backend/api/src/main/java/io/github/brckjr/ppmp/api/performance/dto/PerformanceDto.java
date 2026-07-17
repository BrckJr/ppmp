package io.github.brckjr.ppmp.api.performance.dto;


import io.github.brckjr.ppmp.common.enums.TimeRange;

import java.util.List;

public record PerformanceDto(
    String benchmarkName,
    TimeRange timeRange,
    List<EquityPointDto> equityCurve,
    List<DrawdownPointDto> drawdownSeries,
    List<RollingVolPointDto> rollingVol
) {
}