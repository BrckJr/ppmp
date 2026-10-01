package io.github.brckjr.ppmp.domain.model.holding;

import io.github.brckjr.ppmp.common.enums.AssetClass;
import io.github.brckjr.ppmp.common.enums.Currency;
import io.github.brckjr.ppmp.common.enums.Region;
import io.github.brckjr.ppmp.common.enums.Sector;

import java.math.BigDecimal;
import java.util.UUID;

public record HoldingDetail(
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
    BigDecimal unrealizedGainPct,
    BigDecimal realizedGain,
    boolean priceAvailable
) {
}
