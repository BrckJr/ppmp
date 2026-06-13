package io.github.brckjr.ppmp.api.transactions.dto;

import java.math.BigDecimal;
import java.util.List;

public record TransactionsDto(
        List<TransactionDetailDto> transactions,
        BigDecimal realizedGainsYtd,
        BigDecimal unrealizedGains,
        BigDecimal dividends
) {
}
