package io.github.brckjr.ppmp.domain.model.watchlist;

import io.github.brckjr.ppmp.domain.model.BaseModel;
import io.github.brckjr.ppmp.domain.model.instrument.Instrument;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

// TODO: As soon as further data is available, refactor the service to use the WatchlistItem and not the view anymore!
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

  private WatchlistItem(UUID id, OffsetDateTime createdAt, OffsetDateTime updatedAt, Instrument instrument, String notes, Integer priority) {
    super(id, createdAt, updatedAt);
    this.instrument = instrument;
    this.notes = notes;
    this.priority = priority;
  }

  public static WatchlistItem create(Instrument instrument, String notes, Integer priority) {
    Objects.requireNonNull(instrument, "Instrument cannot be null");
    if (notes != null && notes.length() > 255) {
      throw new IllegalArgumentException("Notes cannot exceed 255 characters");
    }
    return new WatchlistItem(instrument, notes, priority);
  }

  public static WatchlistItem reconstitute(
    UUID id,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    Instrument instrument,
    String notes,
    Integer priority
  ) {
    return new WatchlistItem(id, createdAt, updatedAt, instrument, notes, priority);
  }

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
