package io.github.brckjr.ppmp.domain.service.holding;

import io.github.brckjr.ppmp.domain.enums.AssetClass;
import io.github.brckjr.ppmp.domain.enums.Currency;
import io.github.brckjr.ppmp.domain.enums.Region;
import io.github.brckjr.ppmp.domain.enums.Sector;
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
