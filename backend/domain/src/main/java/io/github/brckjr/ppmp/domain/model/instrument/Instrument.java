package io.github.brckjr.ppmp.domain.model.instrument;

import io.github.brckjr.ppmp.domain.model.BaseModel;

import java.util.Objects;
import java.util.Optional;

public class Instrument extends BaseModel {

    private final String type;
    private final String name;
    private final String ticker;
    private final String currency;
    private final String isin;
    private final String exchange;
    private final String country;
    private final String region;
    private final String sector;


    private Instrument(
            String type, //
            String name, //
            String ticker, //
            String currency, //
            String isin, //
            String exchange, //
            String country, //
            String region, //
            String sector //
    ) {
        super();
        this.type = type;
        this.currency = currency;
        this.name = name;
        this.ticker = ticker;
        this.isin = isin;
        this.exchange = exchange;
        this.country = country;
        this.region = region;
        this.sector = sector;
    }

    public static Instrument create(
            String type, //
            String name, //
            String ticker, //
            String currency, //
            String isin, //
            String exchange, //
            String country, //
            String region, //
            String sector //
    ) {
        Objects.requireNonNull(type, "Instrument type cannot be null");
        Objects.requireNonNull(currency, "Instrument currency cannot be null");
        return new Instrument(type, name, ticker, currency, isin, exchange, country, region, sector);
    }

    public static Instrument reconstitute(
            String type, //
            String name, //
            String ticker, //
            String currency, //
            String isin, //
            String exchange, //
            String country, //
            String region, //
            String sector //
    ) {
        return new Instrument(type, name, ticker, currency, isin, exchange, country, region, sector);
    }


    // --- Domain Behaviors ---


    // --- Getters ---

    public String getType() {
        return type;
    }

    public String getCurrency() {
        return currency;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<String> getTicker() {
        return Optional.ofNullable(ticker);
    }


    public Optional<String> getIsin() {
        return Optional.ofNullable(isin);
    }


    public Optional<String> getExchange() {
        return Optional.ofNullable(exchange);
    }

    public Optional<String> getCountry() {
        return Optional.ofNullable(country);
    }

    public Optional<String> getRegion() {
        return Optional.ofNullable(region);
    }

    public Optional<String> getSector() {
        return Optional.ofNullable(sector);
    }


}
