package io.github.brckjr.ppmp.domain.model.instrument;

import io.github.brckjr.ppmp.domain.model.BaseModel;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

public class InstrumentPrice extends BaseModel {

    private final Instrument instrument;
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
        Objects.requireNonNull(open, "Open price cannot be null");
        Objects.requireNonNull(high, "High price cannot be null");
        Objects.requireNonNull(low, "Low price cannot be null");
        Objects.requireNonNull(close, "Close price cannot be null");
        Objects.requireNonNull(volume, "Volume cannot be null");
        Objects.requireNonNull(currency, "Currency cannot be null");

        return new InstrumentPrice(instrument, open, high, low, close, adjClose, volume, currency, source);
    }

    public static InstrumentPrice reconstitute(
            Instrument instrument,
            BigDecimal open,
            BigDecimal high,
            BigDecimal low,
            BigDecimal close,
            BigDecimal adjClose,
            Long volume,
            String currency,
            String source
    ) {
        return new InstrumentPrice(instrument, open, high, low, close, adjClose, volume, currency, source);
    }

    // --- Domain Behaviors ---


    // --- Getters ---

    public Instrument getInstrument() {
        return instrument;
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
