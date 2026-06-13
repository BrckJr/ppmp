package io.github.brckjr.ppmp.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "instrument", schema = "ppmp")
public class InstrumentEntity extends BaseEntity {

    @Size(max = 100)
    @NotNull
    @Column(name = "type", nullable = false, length = 100)
    private String type;

    @Size(max = 100)
    @Column(name = "name", length = 100)
    private String name;

    @Size(max = 10)
    @Column(name = "ticker", length = 10)
    private String ticker;

    @Size(max = 3)
    @NotNull
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Size(max = 12)
    @Column(name = "isin", length = 12)
    private String isin;

    @Size(max = 100)
    @Column(name = "exchange", length = 100)
    private String exchange;

    @Size(max = 40)
    @Column(name = "country", length = 40)
    private String country;

    @Size(max = 40)
    @Column(name = "region", length = 40)
    private String region;

    @Size(max = 40)
    @Column(name = "sector", length = 40)
    private String sector;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

}