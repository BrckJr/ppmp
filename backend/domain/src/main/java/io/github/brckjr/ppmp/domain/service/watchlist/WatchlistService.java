package io.github.brckjr.ppmp.domain.service.watchlist;

import io.github.brckjr.ppmp.common.enums.AnalystRating;
import io.github.brckjr.ppmp.domain.model.instrument.Instrument;
import io.github.brckjr.ppmp.domain.model.shared.User;
import io.github.brckjr.ppmp.domain.model.watchlist.Watchlist;
import io.github.brckjr.ppmp.domain.model.watchlist.WatchlistItem;
import io.github.brckjr.ppmp.domain.model.watchlist.WatchlistItemView;
import io.github.brckjr.ppmp.domain.repository.InstrumentPriceRepository;
import io.github.brckjr.ppmp.domain.repository.InstrumentRepository;
import io.github.brckjr.ppmp.domain.repository.UserRepository;
import io.github.brckjr.ppmp.domain.repository.WatchlistRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
public class WatchlistService {

  // There is no authentication yet, so everything belongs to this single user.
  static final String DEFAULT_USERNAME = "default";
  static final String DEFAULT_EMAIL = "default@ppmp.local";

  private final WatchlistRepository watchlistRepository;
  private final UserRepository userRepository;
  private final InstrumentRepository instrumentRepository;
  private final InstrumentPriceRepository priceRepository;

  @Inject
  public WatchlistService(
    WatchlistRepository watchlistRepository,
    UserRepository userRepository,
    InstrumentRepository instrumentRepository,
    InstrumentPriceRepository priceRepository
  ) {
    this.watchlistRepository = watchlistRepository;
    this.userRepository = userRepository;
    this.instrumentRepository = instrumentRepository;
    this.priceRepository = priceRepository;
  }

  /** Lists the watchlists of the current user, oldest first. */
  public List<Watchlist> getWatchlists() {
    return watchlistRepository.findByUserId(currentUser().getId()).stream()
      .sorted(Comparator.comparing(Watchlist::getCreatedAt).thenComparing(Watchlist::getId))
      .toList();
  }

  public Watchlist createWatchlist(String name, String description) {
    User user = currentUser();
    Watchlist watchlist = Watchlist.create(user, name, description, null);
    boolean nameTaken = watchlistRepository.findByUserId(user.getId()).stream()
      .anyMatch(existing -> existing.getName().equalsIgnoreCase(watchlist.getName()));
    if (nameTaken) {
      throw new IllegalArgumentException("A watchlist named '" + watchlist.getName() + "' already exists");
    }
    return watchlistRepository.persist(watchlist);
  }

  /** Deletes a watchlist including its items. @throws NoSuchElementException if it does not exist */
  public void deleteWatchlist(UUID watchlistId) {
    findOwnedWatchlist(watchlistId);
    watchlistRepository.deleteById(watchlistId);
  }

  /** @throws NoSuchElementException if the watchlist does not exist */
  public List<WatchlistItemView> getItems(UUID watchlistId) {
    return toViews(findOwnedWatchlist(watchlistId));
  }

  /**
   * @throws NoSuchElementException   if the watchlist does not exist
   * @throws IllegalArgumentException if the instrument is unknown or already on the watchlist
   */
  public WatchlistItemView addItem(UUID watchlistId, UUID instrumentId, String notes, Integer priority) {
    Objects.requireNonNull(instrumentId, "Instrument id cannot be null");
    Watchlist watchlist = findOwnedWatchlist(watchlistId);
    Instrument instrument = instrumentRepository.findById(instrumentId)
      .orElseThrow(() -> new IllegalArgumentException("Unknown instrument: " + instrumentId));

    WatchlistItem item = watchlist.addInstrument(instrument, notes, priority);
    watchlistRepository.update(watchlistId, watchlist);
    return toView(item);
  }

  /** @throws NoSuchElementException if the watchlist or the item does not exist */
  public void removeItem(UUID watchlistId, UUID itemId) {
    Objects.requireNonNull(itemId, "Item id cannot be null");
    Watchlist watchlist = findOwnedWatchlist(watchlistId);
    if (!watchlist.removeItem(itemId)) {
      throw new NoSuchElementException("Watchlist item not found: " + itemId);
    }
    watchlistRepository.update(watchlistId, watchlist);
  }

  private Watchlist findOwnedWatchlist(UUID watchlistId) {
    Objects.requireNonNull(watchlistId, "Watchlist id cannot be null");
    UUID userId = currentUser().getId();
    return watchlistRepository.findById(watchlistId)
      .filter(watchlist -> watchlist.getUser().getId().equals(userId))
      .orElseThrow(() -> new NoSuchElementException("Watchlist not found: " + watchlistId));
  }

  private User currentUser() {
    return userRepository.findByUsername(DEFAULT_USERNAME)
      .orElseGet(() -> userRepository.persist(User.create(DEFAULT_EMAIL, DEFAULT_USERNAME, null, null, "ACTIVE")));
  }

  private List<WatchlistItemView> toViews(Watchlist watchlist) {
    return watchlist.getItems().stream()
      .sorted(Comparator.comparing(WatchlistItem::getCreatedAt).thenComparing(WatchlistItem::getId))
      .map(this::toView)
      .toList();
  }

  // Only price is real (latest stored close); target, rating and ratios are placeholders until a data source exists.
  private WatchlistItemView toView(WatchlistItem item) {
    Instrument instrument = item.getInstrument().orElseThrow();
    String ticker = instrument.getTicker().orElse("");
    int seed = Math.abs(ticker.hashCode() % 1_000_000);

    BigDecimal price = priceRepository.findLatestByInstrumentId(instrument.getId())
      .map(latest -> latest.getClose())
      .orElseGet(() -> money(50 + seed % 450));
    BigDecimal target = price.multiply(BigDecimal.valueOf(1.0 + (seed % 35 - 5) / 100.0)).setScale(2, RoundingMode.HALF_UP);
    AnalystRating rating = AnalystRating.values()[seed % AnalystRating.values().length];

    return new WatchlistItemView(
      item.getId(),
      instrument.getId(),
      ticker,
      instrument.getName().orElse(ticker),
      price,
      target,
      rating,
      BigDecimal.valueOf(10 + seed % 30).setScale(1, RoundingMode.HALF_UP),
      BigDecimal.valueOf(5 + seed % 25).divide(BigDecimal.TEN, 1, RoundingMode.HALF_UP),
      BigDecimal.valueOf(seed % 40).divide(BigDecimal.TEN, 2, RoundingMode.HALF_UP)
    );
  }

  private static BigDecimal money(int value) {
    return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
  }
}
