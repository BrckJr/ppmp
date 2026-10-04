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
import java.util.*;

@ApplicationScoped
public class WatchlistService {

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

  public List<Watchlist> getWatchlists(UUID userId) {
    Objects.requireNonNull(userId, "User id cannot be null");
    return watchlistRepository.findByUserId(userId).stream()
      .sorted(Comparator.comparing(Watchlist::getCreatedAt).thenComparing(Watchlist::getId))
      .toList();
  }

  public Watchlist createWatchlist(UUID userId, String name, String description) {
    User user = loadUser(userId);
    Watchlist watchlist = Watchlist.create(user, name, description, null);
    boolean nameTaken = watchlistRepository.findByUserId(user.getId()).stream()
      .anyMatch(existing -> existing.getName().equalsIgnoreCase(watchlist.getName()));
    if (nameTaken) {
      throw new IllegalArgumentException("A watchlist named '" + watchlist.getName() + "' already exists");
    }
    return watchlistRepository.persist(watchlist);
  }

  public void deleteWatchlist(UUID userId, UUID watchlistId) {
    findOwnedWatchlist(userId, watchlistId);
    watchlistRepository.deleteById(watchlistId);
  }

  public List<WatchlistItemView> getItems(UUID userId, UUID watchlistId) {
    return toViews(findOwnedWatchlist(userId, watchlistId));
  }

  public WatchlistItemView addItem(UUID userId, UUID watchlistId, UUID instrumentId, String notes, Integer priority) {
    Objects.requireNonNull(instrumentId, "Instrument id cannot be null");
    Watchlist watchlist = findOwnedWatchlist(userId, watchlistId);
    Instrument instrument = instrumentRepository.findById(instrumentId)
      .orElseThrow(() -> new IllegalArgumentException("Unknown instrument: " + instrumentId));

    WatchlistItem item = watchlist.addInstrument(instrument, notes, priority);
    watchlistRepository.update(watchlistId, watchlist);
    return toView(item);
  }

  public void removeItem(UUID userId, UUID watchlistId, UUID itemId) {
    Objects.requireNonNull(itemId, "Item id cannot be null");
    Watchlist watchlist = findOwnedWatchlist(userId, watchlistId);
    if (!watchlist.removeItem(itemId)) {
      throw new NoSuchElementException("Watchlist item not found: " + itemId);
    }
    watchlistRepository.update(watchlistId, watchlist);
  }

  private Watchlist findOwnedWatchlist(UUID userId, UUID watchlistId) {
    Objects.requireNonNull(userId, "User id cannot be null");
    Objects.requireNonNull(watchlistId, "Watchlist id cannot be null");
    return watchlistRepository.findById(watchlistId)
      .filter(watchlist -> watchlist.getUser().getId().equals(userId))
      .orElseThrow(() -> new NoSuchElementException("Watchlist not found: " + watchlistId));
  }

  private User loadUser(UUID userId) {
    Objects.requireNonNull(userId, "User id cannot be null");
    return userRepository.findById(userId)
      .orElseThrow(() -> new IllegalStateException("Unknown user: " + userId));
  }

  private List<WatchlistItemView> toViews(Watchlist watchlist) {
    return watchlist.getItems().stream()
      .sorted(Comparator.comparing(WatchlistItem::getCreatedAt).thenComparing(WatchlistItem::getId))
      .map(this::toView)
      .toList();
  }

  // TODO: Only price is real (latest stored close); target, rating and ratios are placeholders until a data source exists.
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
