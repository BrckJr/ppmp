package io.github.brckjr.ppmp.app.transactions.mapper;

import io.github.brckjr.ppmp.api.transactions.dto.TransactionDto;
import io.github.brckjr.ppmp.common.enums.Currency;
import io.github.brckjr.ppmp.common.enums.TransactionType;
import io.github.brckjr.ppmp.domain.model.instrument.Instrument;
import io.github.brckjr.ppmp.domain.model.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionDtoMapperTest {

  private TransactionDtoMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = Mappers.getMapper(TransactionDtoMapper.class);
  }

  @Test
  @DisplayName("Should map transaction to dto including instrument details")
  void toDtoIncludesInstrumentDetails() {
    Instrument instrument = Instrument.create("Apple Inc.", "AAPL", "USD", null, null, "US", "US", "TECHNOLOGY", "STOCK");
    Transaction transaction = Transaction.create(
      java.util.UUID.randomUUID(),
      OffsetDateTime.parse("2026-07-01T10:15:30Z"),
      TransactionType.BUY,
      instrument,
      new BigDecimal("100.250000"),
      new BigDecimal("12.500000"),
      new BigDecimal("1250.000000"),
      Currency.USD,
      "test"
    );

    TransactionDto dto = mapper.toDto(transaction);

    assertThat(dto.instrumentId()).isEqualTo(instrument.getId());
    assertThat(dto.ticker()).isEqualTo("AAPL");
    assertThat(dto.instrumentName()).isEqualTo("Apple Inc.");
    assertThat(dto.quantity()).isEqualByComparingTo("12.5");
    assertThat(dto.unitPrice()).isEqualByComparingTo("100.25");
  }
}
