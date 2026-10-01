package io.github.brckjr.ppmp.domain.model.instrument;

import io.github.brckjr.ppmp.domain.model.BaseModel;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Instrument extends BaseModel {

  private final String name;
  private final String ticker;
  private final String currency;
  private final String isin;
  private final String exchange;
  private final String country;
  private final String region;
  private final String sector;
  private final String type;


  private Instrument(
      String name, //
      String ticker, //
      String currency, //
      String isin, //
      String exchange, //
      String country, //
      String region, //
      String sector, //
      String type //
  ) {
    super();
    this.currency = currency;
    this.name = name;
    this.ticker = ticker;
    this.isin = isin;
    this.exchange = exchange;
    this.country = country;
    this.region = region;
    this.sector = sector;
    this.type = type;
  }

  public static Instrument create(
      String name, //
      String ticker, //
      String currency, //
      String isin, //
      String exchange, //
      String country, //
      String region, //
      String sector, //
      String type //
  ) {
    Objects.requireNonNull(currency, "Instrument currency cannot be null");
    return new Instrument(name, ticker, currency, isin, exchange, country, region, sector, type);
  }

  public static Instrument reconstitute(
      String name, //
      String ticker, //
      String currency, //
      String isin, //
      String exchange, //
      String country, //
      String region, //
      String sector, //
      String type //
  ) {
    return new Instrument(name, ticker, currency, isin, exchange, country, region, sector, type);
  }

  public static Instrument reconstitute(
      UUID id,
      OffsetDateTime createdAt,
      OffsetDateTime updatedAt,
      String name,
      String ticker,
      String currency,
      String isin,
      String exchange,
      String country,
      String region,
      String sector,
      String type
  ) {
    return new Instrument(id, createdAt, updatedAt, name, ticker, currency, isin, exchange, country, region, sector, type);
  }

  private Instrument(
      UUID id,
      OffsetDateTime createdAt,
      OffsetDateTime updatedAt,
      String name,
      String ticker,
      String currency,
      String isin,
      String exchange,
      String country,
      String region,
      String sector,
      String type
  ) {
    super(id, createdAt, updatedAt);
    this.currency = currency;
    this.name = name;
    this.ticker = ticker;
    this.isin = isin;
    this.exchange = exchange;
    this.country = country;
    this.region = region;
    this.sector = sector;
    this.type = type;
  }


  // --- Domain Behaviors ---


  // --- Getters ---
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

  public Optional<String> getType() {
    return Optional.ofNullable(type);
  }


}
