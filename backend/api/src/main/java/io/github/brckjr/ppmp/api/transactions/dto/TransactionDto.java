package io.github.brckjr.ppmp.api.transactions.dto;


import io.github.brckjr.ppmp.common.annotation.Required;
import io.github.brckjr.ppmp.common.enums.Currency;
import io.github.brckjr.ppmp.common.enums.TransactionType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TransactionDto(
  UUID id,
  @Required OffsetDateTime timestamp,
  @Required TransactionType type,
  UUID instrumentId,
  String ticker,
  String instrumentName,
  BigDecimal quantity,
  BigDecimal unitPrice,
  @Required BigDecimal grossAmount,
  @Required Currency currency,
  String comment
) {
}
