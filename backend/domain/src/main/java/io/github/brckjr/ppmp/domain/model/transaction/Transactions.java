package io.github.brckjr.ppmp.domain.model.transaction;

import java.math.BigDecimal;
import java.util.List;

public record Transactions(
        List<TransactionDetail> transactions,
        BigDecimal realizedGainsYtd,
        BigDecimal unrealizedGains,
        BigDecimal dividends
) {}
