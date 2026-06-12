package io.github.brckjr.ppmp.api.transactions.dto;

import io.github.brckjr.ppmp.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionDto(
        String id,
        LocalDate date,
        TransactionType type,
        String ticker,
        BigDecimal shares,
        BigDecimal price,
        BigDecimal amount
) {}
