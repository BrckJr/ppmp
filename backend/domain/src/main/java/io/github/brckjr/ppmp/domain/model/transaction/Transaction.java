package io.github.brckjr.ppmp.domain.model.transaction;

import io.github.brckjr.ppmp.common.enums.Currency;
import io.github.brckjr.ppmp.common.enums.TransactionType;
import io.github.brckjr.ppmp.domain.model.BaseModel;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Transaction extends BaseModel {

  private final OffsetDateTime timestamp;
  private final TransactionType transactionType;
  // TODO: Change the instrument to a reference to an instrument object
  private final String ticker;
  private final BigDecimal quantity;
  private final BigDecimal unitPrice;
  private final BigDecimal grossAmount;
  private final Currency currency;
  private final String comment;

  private Transaction(
      OffsetDateTime timestamp,
      TransactionType transactionType,
      String instrument,
      BigDecimal unitPrice,
      BigDecimal quantity,
      BigDecimal grossAmount,
      Currency currency,
      String comment
  ) {
    super();
    this.timestamp = timestamp;
    this.transactionType = transactionType;
    this.ticker = instrument;
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
      OffsetDateTime timestamp,
      TransactionType transactionType,
      String instrument,
      BigDecimal unitPrice,
      BigDecimal quantity,
      BigDecimal grossAmount,
      Currency currency,
      String comment
  ) {
    super(id, createdAt, updatedAt);
    this.timestamp = timestamp;
    this.transactionType = transactionType;
    this.ticker = instrument;
    this.unitPrice = unitPrice;
    this.quantity = quantity;
    this.grossAmount = grossAmount;
    this.currency = currency;
    this.comment = comment;
  }

  public static Transaction create(
      OffsetDateTime timestamp,
      TransactionType transactionType,
      String instrument,
      BigDecimal unitPrice,
      BigDecimal quantity,
      BigDecimal grossAmount,
      Currency currency,
      String comment
  ) {
    Objects.requireNonNull(timestamp, "Timestamp cannot be null");
    Objects.requireNonNull(instrument, "Instrument cannot be null");
    Objects.requireNonNull(currency, "Currency cannot be null");
    Objects.requireNonNull(grossAmount, "Gross Amount cannot be null");
    Objects.requireNonNull(transactionType, "Transaction Type cannot be null");
    return new Transaction(timestamp, transactionType, instrument, unitPrice, quantity, grossAmount, currency, comment);
  }

  public static Transaction reconstitute(
      UUID id,
      OffsetDateTime createdAt,
      OffsetDateTime updatedAt,
      OffsetDateTime timestamp,
      TransactionType transactionType,
      String instrument,
      BigDecimal unitPrice,
      BigDecimal quantity,
      BigDecimal grossAmount,
      Currency currency,
      String comment
  ) {
    Objects.requireNonNull(id, "Id cannot be null");
    Objects.requireNonNull(createdAt, "CreatedAt cannot be null");
    Objects.requireNonNull(updatedAt, "UpdatedAt cannot be null");
    Objects.requireNonNull(timestamp, "Timestamp cannot be null");
    Objects.requireNonNull(currency, "Currency cannot be null");
    Objects.requireNonNull(grossAmount, "Gross Amount cannot be null");
    Objects.requireNonNull(transactionType, "Transaction Type cannot be null");
    Objects.requireNonNull(instrument, "Instrument cannot be null");
    return new Transaction(
        id,
        createdAt,
        updatedAt,
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

  public String getTicker() {
    return ticker;
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
