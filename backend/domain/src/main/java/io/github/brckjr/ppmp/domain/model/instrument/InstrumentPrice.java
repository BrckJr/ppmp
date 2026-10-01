package io.github.brckjr.ppmp.domain.model.instrument;

import io.github.brckjr.ppmp.domain.model.BaseModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class InstrumentPrice extends BaseModel {

  private final Instrument instrument;
  private final LocalDate priceDate;
  private final BigDecimal open;
  private final BigDecimal high;
  private final BigDecimal low;
  private final BigDecimal close;
  private final BigDecimal adjClose;
  private final Long volume;
  private final String currency;
  private final String source;

  private InstrumentPrice(
      Instrument instrument,
      LocalDate priceDate,
      BigDecimal open,
      BigDecimal high,
      BigDecimal low,
      BigDecimal close,
      BigDecimal adjClose,
      Long volume,
      String currency,
      String source
  ) {
    super();
    this.instrument = instrument;
    this.priceDate = priceDate;
    this.open = open;
    this.high = high;
    this.low = low;
    this.close = close;
    this.adjClose = adjClose;
    this.volume = volume;
    this.currency = currency;
    this.source = source;
  }

  public static InstrumentPrice create(
      Instrument instrument,
      LocalDate priceDate,
      BigDecimal open,
      BigDecimal high,
      BigDecimal low,
      BigDecimal close,
      BigDecimal adjClose,
      Long volume,
      String currency,
      String source
  ) {
    Objects.requireNonNull(instrument, "Instrument cannot be null");
    Objects.requireNonNull(priceDate, "Price date cannot be null");
    Objects.requireNonNull(open, "Open price cannot be null");
    Objects.requireNonNull(high, "High price cannot be null");
    Objects.requireNonNull(low, "Low price cannot be null");
    Objects.requireNonNull(close, "Close price cannot be null");
    Objects.requireNonNull(volume, "Volume cannot be null");
    Objects.requireNonNull(currency, "Currency cannot be null");

    return new InstrumentPrice(instrument, priceDate, open, high, low, close, adjClose, volume, currency, source);
  }

  public static InstrumentPrice reconstitute(
      UUID id,
      OffsetDateTime createdAt,
      OffsetDateTime updatedAt,
      Instrument instrument,
      LocalDate priceDate,
      BigDecimal open,
      BigDecimal high,
      BigDecimal low,
      BigDecimal close,
      BigDecimal adjClose,
      Long volume,
      String currency,
      String source
  ) {
    return new InstrumentPrice(id, createdAt, updatedAt, instrument, priceDate, open, high, low, close, adjClose, volume, currency, source);
  }

  private InstrumentPrice(
      UUID id,
      OffsetDateTime createdAt,
      OffsetDateTime updatedAt,
      Instrument instrument,
      LocalDate priceDate,
      BigDecimal open,
      BigDecimal high,
      BigDecimal low,
      BigDecimal close,
      BigDecimal adjClose,
      Long volume,
      String currency,
      String source
  ) {
    super(id, createdAt, updatedAt);
    this.instrument = instrument;
    this.priceDate = priceDate;
    this.open = open;
    this.high = high;
    this.low = low;
    this.close = close;
    this.adjClose = adjClose;
    this.volume = volume;
    this.currency = currency;
    this.source = source;
  }

  // --- Domain Behaviors ---


  // --- Getters ---

  public Instrument getInstrument() {
    return instrument;
  }

  public LocalDate getPriceDate() {
    return priceDate;
  }

  public BigDecimal getOpen() {
    return open;
  }

  public BigDecimal getHigh() {
    return high;
  }

  public BigDecimal getLow() {
    return low;
  }

  public BigDecimal getClose() {
    return close;
  }

  public Optional<BigDecimal> getAdjClose() {
    return Optional.ofNullable(adjClose);
  }

  public Long getVolume() {
    return volume;
  }

  public String getCurrency() {
    return currency;
  }

  public Optional<String> getSource() {
    return Optional.ofNullable(source);
  }

}
