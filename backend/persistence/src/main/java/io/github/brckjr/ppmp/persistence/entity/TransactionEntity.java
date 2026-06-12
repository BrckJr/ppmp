package io.github.brckjr.ppmp.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.time.OffsetDateTime;

@Entity
@Table(name = "transaction", schema = "ppmp")
public class TransactionEntity extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "portfolio_uuid", nullable = false)
    private PortfolioEntity portfolio;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "instrument_uuid", nullable = true)
    private InstrumentEntity instrument;

    @Size(max = 20)
    @Column(name = "transaction_type", length = 20)
    private String transactionType;

    @NotNull
    @Column(name = "timestamp", nullable = false)
    private OffsetDateTime timestamp;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "quantity")
    private Double quantity;

    @NotNull
    @Column(name = "gross_amount", nullable = false)
    private Double grossAmount;

    @Size(max = 3)
    @NotNull
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Size(max = 255)
    @Column(name = "comment")
    private String comment;

    public PortfolioEntity getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(PortfolioEntity portfolio) {
        this.portfolio = portfolio;
    }

    public InstrumentEntity getInstrument() {
        return instrument;
    }

    public void setInstrument(InstrumentEntity instrument) {
        this.instrument = instrument;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(Double grossAmount) {
        this.grossAmount = grossAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}