package io.github.brckjr.ppmp.domain.model.performance;

import io.github.brckjr.ppmp.domain.enums.TimeRange;

import java.util.List;

public record PerformanceMetrics(
        String benchmarkName,
        TimeRange timeRange,
        List<EquityPoint> equityCurve,
        List<DrawdownPoint> drawdownSeries,
        List<RollingVolPoint> rollingVol
) {}
