package io.github.brckjr.ppmp.domain.service.portfolio;

import io.github.brckjr.ppmp.common.enums.AssetClass;
import io.github.brckjr.ppmp.common.enums.Currency;
import io.github.brckjr.ppmp.common.enums.Region;
import io.github.brckjr.ppmp.common.enums.Sector;
import io.github.brckjr.ppmp.domain.model.holding.HoldingDetail;
import io.github.brckjr.ppmp.domain.model.holding.Holdings;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PortfolioService {

  public Holdings getHoldings() {
    return new Holdings(List.of());
  }

  public HoldingDetail getHolding(UUID id) {
    return new HoldingDetail(
        id,
        "UNKNOWN",
        "Unknown Holding",
        Sector.TECHNOLOGY,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        Currency.USD,
        BigDecimal.ZERO,
        AssetClass.STOCKS,
        Region.US,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO
    );
  }
}
