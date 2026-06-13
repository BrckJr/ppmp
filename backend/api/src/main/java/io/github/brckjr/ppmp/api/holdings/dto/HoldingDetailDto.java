package io.github.brckjr.ppmp.api.holdings.dto;

import io.github.brckjr.ppmp.domain.enums.AssetClass;
import io.github.brckjr.ppmp.domain.enums.Currency;
import io.github.brckjr.ppmp.domain.enums.Region;
import io.github.brckjr.ppmp.domain.enums.Sector;

import java.math.BigDecimal;
import java.util.UUID;

public record HoldingDetailDto(
        UUID id,
        String ticker,
        String name,
        Sector sector,
        BigDecimal shares,
        BigDecimal avgCost,
        BigDecimal unitPrice,
        BigDecimal value,
        BigDecimal gain,
        BigDecimal gainPct,
        Currency currency,
        BigDecimal price,
        AssetClass assetClass,
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
) {
}
