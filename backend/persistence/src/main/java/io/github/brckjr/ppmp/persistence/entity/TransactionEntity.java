package io.github.brckjr.ppmp.persistence.entity;

import io.github.brckjr.ppmp.common.enums.Currency;
import io.github.brckjr.ppmp.common.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "transaction", schema = "ppmp")
public class TransactionEntity extends BaseEntity {

  @Column(name = "ticker", length = 4)
  private String ticker;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "transaction_type", nullable = false, length = 20)
  private TransactionType transactionType;

  @NotNull
  @Column(name = "timestamp", nullable = false)
  private OffsetDateTime timestamp;

  @Column(name = "unit_price", precision = 19, scale = 6)
  private BigDecimal unitPrice;

  @Column(name = "quantity", precision = 19, scale = 6)
  private BigDecimal quantity;

  @NotNull
  @Column(name = "gross_amount", nullable = false, precision = 19, scale = 6)
  private BigDecimal grossAmount;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "currency", nullable = false, length = 3)
  private Currency currency;

  @Size(max = 255)
  @Column(name = "comment")
  private String comment;

  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  public TransactionType getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(TransactionType transactionType) {
    this.transactionType = transactionType;
  }

  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public BigDecimal getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(BigDecimal unitPrice) {
    this.unitPrice = unitPrice;
  }

  public BigDecimal getQuantity() {
    return quantity;
  }

  public void setQuantity(BigDecimal quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getGrossAmount() {
    return grossAmount;
  }

  public void setGrossAmount(BigDecimal grossAmount) {
    this.grossAmount = grossAmount;
  }

  public Currency getCurrency() {
    return currency;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
}
