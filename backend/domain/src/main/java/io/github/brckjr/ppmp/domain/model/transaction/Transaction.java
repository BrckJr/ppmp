package io.github.brckjr.ppmp.domain.model.transaction;

import io.github.brckjr.ppmp.domain.model.BaseModel;
import io.github.brckjr.ppmp.domain.model.instrument.Instrument;
import io.github.brckjr.ppmp.domain.model.portfolio.Portfolio;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;

public class Transaction extends BaseModel {
    private final Portfolio portfolio;
    private final Instrument instrument;
    private final String transactionType;
    private final OffsetDateTime timestamp;
    private final Double unitprice;
    private final Double quantity;
    private final Double grossAmount;
    private final String currency;
    private final String comment;

    private Transaction(
            Portfolio portfolio,
            Instrument instrument,
            String transactionType,
            OffsetDateTime timestamp,
            Double unitprice,
            Double quantity,
            Double grossAmount,
            String currency,
            String comment
    ){
        super();
        this.portfolio = portfolio;
        this.instrument = instrument;
        this.transactionType = transactionType;
        this.timestamp = timestamp;
        this.unitprice = unitprice;
        this.quantity = quantity;
        this.grossAmount = grossAmount;
        this.currency = currency;
        this.comment = comment;
    }

    public static Transaction create(
            Portfolio portfolio,
            Instrument instrument,
            String transactionType,
            OffsetDateTime timestamp,
            Double unitprice,
            Double quantity,
            Double grossAmount,
            String currency,
            String comment
    ) {
        Objects.requireNonNull(portfolio, "Portfolio cannot be null");
        Objects.requireNonNull(timestamp, "Timestamp cannot be null");
        Objects.requireNonNull(grossAmount, "Gross Amount cannot be null");
        Objects.requireNonNull(currency, "Currency cannot be null");
        return new Transaction(portfolio, instrument, transactionType, timestamp, unitprice, quantity, grossAmount, currency, comment);
    }

    public static Transaction reconstitute(
            Portfolio portfolio,
            Instrument instrument,
            String transactionType,
            OffsetDateTime timestamp,
            Double unitprice,
            Double quantity,
            Double grossAmount,
            String currency,
            String comment
    ){
        return new Transaction(portfolio, instrument, transactionType, timestamp, unitprice, quantity, grossAmount, currency, comment);
    }

    // --- Domain Behaviors ---


    // --- Getters ---
    public Portfolio getPortfolio() { return portfolio; }

    public Optional<Instrument> getInstrument() { return Optional.ofNullable(instrument); }

    public Optional<String> getTransactionType() { return Optional.ofNullable(transactionType); }

    public OffsetDateTime getTimestamp() { return timestamp; }

    public Optional<Double> getUnitPrice() { return Optional.ofNullable(unitprice); }

    public Optional<Double> getQuantity() { return Optional.ofNullable(quantity); }

    public Double getGrossAmount() { return grossAmount; }

    public String getCurrency() { return currency; }

    public Optional<String> getComment() { return Optional.ofNullable(comment); }

}
