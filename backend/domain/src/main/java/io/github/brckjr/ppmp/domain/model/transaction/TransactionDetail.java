package io.github.brckjr.ppmp.domain.model.transaction;

import io.github.brckjr.ppmp.domain.enums.Currency;
import io.github.brckjr.ppmp.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionDetail(
        UUID id,
        LocalDate date,
        TransactionType type,
        String ticker,
        BigDecimal shares,
        BigDecimal price,
        BigDecimal amount,
        Currency currency
) {}
