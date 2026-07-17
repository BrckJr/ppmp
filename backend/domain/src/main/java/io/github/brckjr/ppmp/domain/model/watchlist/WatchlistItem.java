package io.github.brckjr.ppmp.domain.model.watchlist;

import io.github.brckjr.ppmp.domain.model.BaseModel;
import io.github.brckjr.ppmp.domain.model.instrument.Instrument;

import java.util.Objects;
import java.util.Optional;

public class WatchlistItem extends BaseModel {
  private final Instrument instrument;
  private final String notes;
  private final Integer priority;

  private WatchlistItem(Instrument instrument, String notes, Integer priority) {
    super();
    this.instrument = instrument;
    this.notes = notes;
    this.priority = priority;
  }

  public static WatchlistItem create(Instrument instrument, String notes, Integer priority) {
    Objects.requireNonNull(instrument, "Instrument cannot be null");
    return new WatchlistItem(instrument, notes, priority);
  }

  public static WatchlistItem reconstitute(Instrument instrument, String notes, Integer priority) {
    return new WatchlistItem(instrument, notes, priority);
  }

  // --- Domain Behaviors ---


  // --- Getters ---
  public Optional<Instrument> getInstrument() {
    return Optional.ofNullable(instrument);
  }

  public Optional<String> getNotes() {
    return Optional.ofNullable(notes);
  }

  public Optional<Integer> getPriority() {
    return Optional.ofNullable(priority);
  }

}
