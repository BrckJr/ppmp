package io.github.brckjr.ppmp.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "watchlist_item", schema = "ppmp")
public class WatchlistItemEntity extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "watchlist_uuid", nullable = false)
    private WatchlistEntity watchlist;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "instrument_uuid", nullable = false)
    private InstrumentEntity instrument;

    @Size(max = 255)
    @Column(name = "notes")
    private String notes;

    @Column(name = "priority")
    private Integer priority;

    public WatchlistEntity getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(WatchlistEntity watchlist) {
        this.watchlist = watchlist;
    }

    public InstrumentEntity getInstrument() {
        return instrument;
    }

    public void setInstrument(InstrumentEntity instrument) {
        this.instrument = instrument;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

}