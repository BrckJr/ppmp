package io.github.brckjr.ppmp.domain.model.watchlist;

import io.github.brckjr.ppmp.domain.model.BaseModel;
import io.github.brckjr.ppmp.domain.model.instrument.Instrument;
import io.github.brckjr.ppmp.domain.model.shared.User;

import java.time.OffsetDateTime;
import java.util.*;

public final class Watchlist extends BaseModel {
  public static final String DEFAULT_NAME = "default watchlist";

  private final User user;
  private final String name;
  private final String description;
  private final List<WatchlistItem> items;

  private Watchlist(User user, String name, String description, List<WatchlistItem> items) {
    super();
    this.user = user;
    this.name = name;
    this.description = description;
    this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
  }

  private Watchlist(
      UUID id,
      OffsetDateTime createdAt,
      OffsetDateTime updatedAt,
      User user,
      String name,
      String description,
      List<WatchlistItem> items
  ) {
    super(id, createdAt, updatedAt);
    this.user = user;
    this.name = name;
    this.description = description;
    this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
  }

  public static Watchlist create(User user, String name, String description, List<WatchlistItem> items) {
    Objects.requireNonNull(user, "User cannot be null");
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Watchlist name is required");
    }
    name = name.trim();
    if (name.length() > 100) {
      throw new IllegalArgumentException("Watchlist name cannot exceed 100 characters");
    }
    description = description == null || description.isBlank() ? null : description.trim();
    if (description != null && description.length() > 255) {
      throw new IllegalArgumentException("Watchlist description cannot exceed 255 characters");
    }
    return new Watchlist(user, name, description, items);
  }

  public static Watchlist reconstitute(
      UUID id,
      OffsetDateTime createdAt,
      OffsetDateTime updatedAt,
      User user,
      String name,
      String description,
      List<WatchlistItem> items
  ) {
    return new Watchlist(id, createdAt, updatedAt, user, name, description, items);
  }

  // --- Domain Behaviors ---

  /** Adds an instrument to this watchlist; an instrument can only be on a watchlist once. */
  public WatchlistItem addInstrument(Instrument instrument, String notes, Integer priority) {
    Objects.requireNonNull(instrument, "Instrument cannot be null");
    boolean alreadyPresent = items.stream()
      .anyMatch(item -> item.getInstrument().map(existing -> existing.getId().equals(instrument.getId())).orElse(false));
    if (alreadyPresent) {
      throw new IllegalArgumentException("Instrument is already on watchlist '" + name + "'");
    }
    WatchlistItem item = WatchlistItem.create(instrument, notes, priority);
    items.add(item);
    return item;
  }

  public boolean removeItem(UUID itemId) {
    return items.removeIf(item -> item.getId().equals(itemId));
  }

  // --- Getters ---

  public User getUser() {
    return user;
  }

  public String getName() {
    return name;
  }

  public Optional<String> getDescription() {
    return Optional.ofNullable(description);
  }

  public List<WatchlistItem> getItems() {
    return Collections.unmodifiableList(items);
  }

}
