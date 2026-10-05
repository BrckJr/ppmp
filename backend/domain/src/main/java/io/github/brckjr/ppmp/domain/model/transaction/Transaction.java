package io.github.brckjr.ppmp.domain.model.transaction;

import io.github.brckjr.ppmp.common.enums.Currency;
import io.github.brckjr.ppmp.common.enums.TransactionType;
import io.github.brckjr.ppmp.domain.model.BaseModel;
import io.github.brckjr.ppmp.domain.model.instrument.Instrument;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Transaction extends BaseModel {

  private final UUID userId;
  private final OffsetDateTime timestamp;
  private final TransactionType transactionType;
  private final Instrument instrument;
  private final BigDecimal quantity;
  private final BigDecimal unitPrice;
  private final BigDecimal grossAmount;
  private final Currency currency;
  private final String comment;

  private Transaction(
      UUID userId,
      OffsetDateTime timestamp,
      TransactionType transactionType,
      Instrument instrument,
      BigDecimal unitPrice,
      BigDecimal quantity,
      BigDecimal grossAmount,
      Currency currency,
      String comment
  ) {
    super();
    this.userId = userId;
    this.timestamp = timestamp;
    this.transactionType = transactionType;
    this.instrument = instrument;
    this.unitPrice = unitPrice;
    this.quantity = quantity;
    this.grossAmount = grossAmount;
    this.currency = currency;
    this.comment = comment;
  }

  private Transaction(
      UUID id,
      OffsetDateTime createdAt,
      OffsetDateTime updatedAt,
      UUID userId,
      OffsetDateTime timestamp,
      TransactionType transactionType,
      Instrument instrument,
      BigDecimal unitPrice,
      BigDecimal quantity,
      BigDecimal grossAmount,
      Currency currency,
      String comment
  ) {
    super(id, createdAt, updatedAt);
    this.userId = userId;
    this.timestamp = timestamp;
    this.transactionType = transactionType;
    this.instrument = instrument;
    this.unitPrice = unitPrice;
    this.quantity = quantity;
    this.grossAmount = grossAmount;
    this.currency = currency;
    this.comment = comment;
  }

  public static Transaction create(
      UUID userId,
      OffsetDateTime timestamp,
      TransactionType transactionType,
      Instrument instrument,
      BigDecimal unitPrice,
      BigDecimal quantity,
      BigDecimal grossAmount,
      Currency currency,
      String comment
  ) {
    Objects.requireNonNull(userId, "User id cannot be null");
    Objects.requireNonNull(timestamp, "Timestamp cannot be null");
    Objects.requireNonNull(currency, "Currency cannot be null");
    Objects.requireNonNull(grossAmount, "Gross Amount cannot be null");
    Objects.requireNonNull(transactionType, "Transaction Type cannot be null");
    if (grossAmount.signum() <= 0) {
      throw new IllegalArgumentException("Gross Amount must be positive");
    }
    boolean requiresInstrument = transactionType == TransactionType.BUY
      || transactionType == TransactionType.SELL
      || transactionType == TransactionType.DIVIDEND;
    if (requiresInstrument) {
      if (instrument == null) {
        throw new IllegalArgumentException("Instrument is required for buy, sell and dividend transactions");
      }
    } else {
      instrument = null;
    }
    if (transactionType == TransactionType.BUY || transactionType == TransactionType.SELL) {
      if (quantity == null || quantity.signum() <= 0) {
        throw new IllegalArgumentException("Quantity must be positive for buy and sell transactions");
      }
      if (unitPrice == null || unitPrice.signum() <= 0) {
        throw new IllegalArgumentException("Unit price must be positive for buy and sell transactions");
      }
    }
    return new Transaction(userId, timestamp, transactionType, instrument, unitPrice, quantity, grossAmount, currency, comment);
  }

  public static Transaction reconstitute(
      UUID id,
      OffsetDateTime createdAt,
      OffsetDateTime updatedAt,
      UUID userId,
      OffsetDateTime timestamp,
      TransactionType transactionType,
      Instrument instrument,
      BigDecimal unitPrice,
      BigDecimal quantity,
      BigDecimal grossAmount,
      Currency currency,
      String comment
  ) {
    Objects.requireNonNull(id, "Id cannot be null");
    Objects.requireNonNull(createdAt, "CreatedAt cannot be null");
    Objects.requireNonNull(updatedAt, "UpdatedAt cannot be null");
    Objects.requireNonNull(userId, "User id cannot be null");
    Objects.requireNonNull(timestamp, "Timestamp cannot be null");
    Objects.requireNonNull(currency, "Currency cannot be null");
    Objects.requireNonNull(grossAmount, "Gross Amount cannot be null");
    Objects.requireNonNull(transactionType, "Transaction Type cannot be null");
    return new Transaction(
        id,
        createdAt,
        updatedAt,
        userId,
        timestamp,
        transactionType,
        instrument,
        unitPrice,
        quantity,
        grossAmount,
        currency,
        comment
    );
  }

  // --- Domain Behaviors ---


  // --- Getters ---

  public UUID getUserId() {
    return userId;
  }

  public Optional<Instrument> getInstrument() {
    return Optional.ofNullable(instrument);
  }

  public String getTicker() {
    return instrument == null ? null : instrument.getTicker().orElse(null);
  }

  public TransactionType getTransactionType() {
    return transactionType;
  }

  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public Optional<BigDecimal> getUnitPrice() {
    return Optional.ofNullable(unitPrice);
  }

  public Optional<BigDecimal> getQuantity() {
    return Optional.ofNullable(quantity);
  }

  public BigDecimal getGrossAmount() {
    return grossAmount;
  }

  public Currency getCurrency() {
    return currency;
  }

  public Optional<String> getComment() {
    return Optional.ofNullable(comment);
  }

}
