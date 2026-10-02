package io.github.brckjr.ppmp.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "instrument_prices",
        schema = "ppmp",
        indexes = {
                @Index(name = "idx_instrument_prices_instrument_uuid", columnList = "instrument_uuid")
        }
)
public class InstrumentPriceEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "instrument_uuid", nullable = false)
    private InstrumentEntity instrument;

    @NotNull
    @Column(name = "price_date", nullable = false)
    private LocalDate priceDate;

    @NotNull
    @Column(name = "open", nullable = false, precision = 15, scale = 4)
    private BigDecimal open;

    @NotNull
    @Column(name = "high", nullable = false, precision = 15, scale = 4)
    private BigDecimal high;

    @NotNull
    @Column(name = "low", nullable = false, precision = 15, scale = 4)
    private BigDecimal low;

    @NotNull
    @Column(name = "close", nullable = false, precision = 15, scale = 4)
    private BigDecimal close;

    @Column(name = "adj_close", precision = 15, scale = 4)
    private BigDecimal adjClose;

    @NotNull
    @Column(name = "volume", nullable = false)
    private Long volume;

    @Size(max = 3)
    @NotNull
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Size(max = 50)
    @Column(name = "source", length = 50)
    private String source;

    public InstrumentEntity getInstrument() {
        return instrument;
    }

    public void setInstrument(InstrumentEntity instrumentUuid) {
        this.instrument = instrumentUuid;
    }

    public LocalDate getPriceDate() {
        return priceDate;
    }

    public void setPriceDate(LocalDate priceDate) {
        this.priceDate = priceDate;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getAdjClose() {
        return adjClose;
    }

    public void setAdjClose(BigDecimal adjClose) {
        this.adjClose = adjClose;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
