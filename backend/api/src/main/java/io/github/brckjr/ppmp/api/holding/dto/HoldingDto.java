package io.github.brckjr.ppmp.api.holding.dto;

import io.github.brckjr.ppmp.domain.enums.AssetClass;
import io.github.brckjr.ppmp.domain.enums.Region;
import io.github.brckjr.ppmp.domain.enums.Sector;

import java.math.BigDecimal;

public record HoldingDto(
        String ticker,
        String name,
        BigDecimal shares,
        BigDecimal avgCost,
        BigDecimal price,
        AssetClass assetClass,
        Sector sector,
        Region region,
        BigDecimal marketCapBillions,
        BigDecimal pe,
        BigDecimal forwardPe,
        BigDecimal eps,
        BigDecimal revenueGrowthPct,
        BigDecimal roePct,
        BigDecimal debtEquity,
        BigDecimal dividendYieldPct,
        BigDecimal marketValue,
        BigDecimal costBasis,
        BigDecimal unrealizedGain,
        BigDecimal unrealizedGainPct
) {}
