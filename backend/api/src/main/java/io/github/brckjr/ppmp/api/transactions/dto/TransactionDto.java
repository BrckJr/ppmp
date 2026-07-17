package io.github.brckjr.ppmp.api.transactions.dto;


import io.github.brckjr.ppmp.common.enums.Currency;
import io.github.brckjr.ppmp.common.enums.TransactionType;
import io.smallrye.common.constraint.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TransactionDto(
    UUID id,
    @NotNull OffsetDateTime timestamp,
    @NotNull TransactionType type,
    String ticker,
    BigDecimal quantity,
    BigDecimal unitPrice,
    @NotNull BigDecimal grossAmount,
    @NotNull Currency currency,
    String comment
) {
}
