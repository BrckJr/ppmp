package io.github.brckjr.ppmp.app.transactions.mapper;

import io.github.brckjr.ppmp.api.transactions.dto.TransactionDto;
import io.github.brckjr.ppmp.common.enums.Currency;
import io.github.brckjr.ppmp.common.enums.TransactionType;
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
  @DisplayName("Should map TransactionDto to domain keeping quantity and unit price in the correct order")
  void toDomainKeepsQuantityAndUnitPriceInTheRightOrder() {
    TransactionDto dto = new TransactionDto(
      null,
      OffsetDateTime.parse("2026-07-01T10:15:30Z"),
      TransactionType.BUY,
      "AAPL",
      new BigDecimal("12.500000"),
      new BigDecimal("100.250000"),
      new BigDecimal("1250.000000"),
      Currency.USD,
      "test"
    );

    var domain = mapper.toDomain(dto);

    assertThat(domain).isNotNull();
    assertThat(domain.getQuantity()).contains(new BigDecimal("12.500000"));
    assertThat(domain.getUnitPrice()).contains(new BigDecimal("100.250000"));
    assertThat(domain.getTicker()).isEqualTo("AAPL");
  }

  @Test
  @DisplayName("Should return null safely when mapping null source dto")
  void shouldMapNullSafely() {
    var domain = mapper.toDomain(null);

    assertThat(domain).isNull();
  }
}