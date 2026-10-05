package io.github.brckjr.ppmp.domain.service.watchlist;

import io.github.brckjr.ppmp.domain.model.instrument.Instrument;
import io.github.brckjr.ppmp.domain.model.instrument.InstrumentPrice;
import io.github.brckjr.ppmp.domain.model.shared.User;
import io.github.brckjr.ppmp.domain.model.watchlist.Watchlist;
import io.github.brckjr.ppmp.domain.model.watchlist.WatchlistItemView;
import io.github.brckjr.ppmp.domain.repository.InstrumentPriceRepository;
import io.github.brckjr.ppmp.domain.repository.InstrumentRepository;
import io.github.brckjr.ppmp.domain.repository.UserRepository;
import io.github.brckjr.ppmp.domain.repository.WatchlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WatchlistServiceTest {

  private final InMemory<Watchlist> watchlists = new InMemory<>();
  private final InMemory<User> users = new InMemory<>();
  private final InMemory<Instrument> instruments = new InMemory<>();
  private WatchlistService service;
  private Instrument apple;
  private UUID userId;

  @BeforeEach
  void setUp() {
    service = new WatchlistService(
      new WatchlistRepository() {
        @Override
        public Optional<Watchlist> findById(UUID id) {
          return watchlists.findById(id);
        }

        @Override
        public List<Watchlist> findAll() {
          return watchlists.findAll();
        }

        @Override
        public Watchlist persist(Watchlist d) {
          return watchlists.persist(d);
        }

        @Override
        public Watchlist update(UUID id, Watchlist d) {
          return watchlists.persist(d);
        }

        @Override
        public void deleteById(UUID id) {
          watchlists.store.remove(id);
        }

        @Override
        public long count() {
          return watchlists.store.size();
        }

        @Override
        public List<Watchlist> findByUserId(UUID userId) {
          return watchlists.findAll().stream().filter(w -> w.getUser().getId().equals(userId)).toList();
        }
      },
      new UserRepository() {
        @Override
        public Optional<User> findById(UUID id) {
          return users.findById(id);
        }

        @Override
        public List<User> findAll() {
          return users.findAll();
        }

        @Override
        public User persist(User d) {
          return users.persist(d);
        }

        @Override
        public User update(UUID id, User d) {
          return users.persist(d);
        }

        @Override
        public void deleteById(UUID id) {
          users.store.remove(id);
        }

        @Override
        public long count() {
          return users.store.size();
        }

        @Override
        public Optional<User> findByUsername(String username) {
          return users.findAll().stream().filter(u -> u.getUsername().equals(username)).findFirst();
        }

        @Override
        public Optional<User> findByEmail(String email) {
          return users.findAll().stream().filter(u -> u.getEmail().equals(email)).findFirst();
        }
      },
      new InstrumentRepository() {
        @Override
        public Optional<Instrument> findById(UUID id) {
          return instruments.findById(id);
        }

        @Override
        public List<Instrument> findAll() {
          return instruments.findAll();
        }

        @Override
        public Instrument persist(Instrument d) {
          return instruments.persist(d);
        }

        @Override
        public Instrument update(UUID id, Instrument d) {
          return instruments.persist(d);
        }

        @Override
        public void deleteById(UUID id) {
          instruments.store.remove(id);
        }

        @Override
        public long count() {
          return instruments.store.size();
        }

        @Override
        public Optional<Instrument> findByTicker(String t) {
          return Optional.empty();
        }

        @Override
        public Optional<Instrument> findByIsin(String i) {
          return Optional.empty();
        }
      },
      new InstrumentPriceRepository() {
        @Override
        public Optional<InstrumentPrice> findById(UUID id) {
          return Optional.empty();
        }

        @Override
        public List<InstrumentPrice> findAll() {
          return List.of();
        }

        @Override
        public InstrumentPrice persist(InstrumentPrice d) {
          return d;
        }

        @Override
        public InstrumentPrice update(UUID id, InstrumentPrice d) {
          return d;
        }

        @Override
        public void deleteById(UUID id) {
        }

        @Override
        public long count() {
          return 0;
        }

        @Override
        public List<InstrumentPrice> findByInstrumentId(UUID id, LocalDate from, LocalDate to) {
          return List.of();
        }

        @Override
        public Optional<InstrumentPrice> findLatestByInstrumentId(UUID id) {
          return Optional.empty();
        }

        @Override
        public Optional<InstrumentPrice> findByInstrumentIdAndDate(UUID id, LocalDate d) {
          return Optional.empty();
        }
      }
    );
    userId = users.persist(User.register("tester@ppmp.local", "tester", "hash")).getId();
    apple = instruments.persist(Instrument.create("Apple Inc.", "AAPL", "USD", null, null, "US", "US", "TECHNOLOGY", "STOCK"));
  }

  @Test
  void startsWithoutWatchlists() {
    assertThat(service.getWatchlists(userId)).isEmpty();
  }

  @Test
  void createsWatchlistsWithUniqueNames() {
    Watchlist created = service.createWatchlist(userId, "Tech", "Tech stocks");
    service.createWatchlist(userId, "Dividends", null);

    assertThat(service.getWatchlists(userId)).extracting(Watchlist::getName).containsExactly("Tech", "Dividends");
    assertThat(created.getDescription()).contains("Tech stocks");
    assertThatThrownBy(() -> service.createWatchlist(userId, " tech ", null)).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> service.createWatchlist(userId, " ", null)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void deletesAnyWatchlist() {
    Watchlist tech = service.createWatchlist(userId, "Tech", null);
    service.addItem(userId, tech.getId(), apple.getId(), null, null);

    service.deleteWatchlist(userId, tech.getId());

    assertThat(service.getWatchlists(userId)).isEmpty();
    assertThatThrownBy(() -> service.deleteWatchlist(userId, tech.getId())).isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void addsAndRemovesInstruments() {
    UUID watchlistId = service.createWatchlist(userId, "Tech", null).getId();

    WatchlistItemView view = service.addItem(userId, watchlistId, apple.getId(), null, null);

    assertThat(view.ticker()).isEqualTo("AAPL");
    assertThat(view.instrumentId()).isEqualTo(apple.getId());
    assertThat(view.price()).isPositive();
    assertThat(service.getItems(userId, watchlistId)).extracting(WatchlistItemView::id).containsExactly(view.id());

    service.removeItem(userId, watchlistId, view.id());
    assertThat(service.getItems(userId, watchlistId)).isEmpty();
  }

  @Test
  void rejectsDuplicatesUnknownInstrumentsAndUnknownWatchlists() {
    UUID watchlistId = service.createWatchlist(userId, "Tech", null).getId();
    service.addItem(userId, watchlistId, apple.getId(), null, null);

    assertThatThrownBy(() -> service.addItem(userId, watchlistId, apple.getId(), null, null)).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> service.addItem(userId, watchlistId, UUID.randomUUID(), null, null)).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> service.addItem(userId, UUID.randomUUID(), apple.getId(), null, null)).isInstanceOf(NoSuchElementException.class);
    assertThatThrownBy(() -> service.removeItem(userId, watchlistId, UUID.randomUUID())).isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void keepsWatchlistsOfDifferentUsersApart() {
    UUID otherUser = users.persist(User.register("other@ppmp.local", "other", "hash")).getId();
    UUID mine = service.createWatchlist(userId, "Tech", null).getId();
    service.addItem(userId, mine, apple.getId(), null, null);
    UUID theirs = service.createWatchlist(otherUser, "Tech", null).getId();

    assertThat(service.getWatchlists(otherUser)).extracting(Watchlist::getId).containsExactly(theirs);
    assertThatThrownBy(() -> service.getItems(otherUser, mine)).isInstanceOf(NoSuchElementException.class);
    assertThatThrownBy(() -> service.addItem(otherUser, mine, apple.getId(), null, null)).isInstanceOf(NoSuchElementException.class);
    assertThatThrownBy(() -> service.deleteWatchlist(otherUser, mine)).isInstanceOf(NoSuchElementException.class);
    assertThat(service.getItems(userId, mine)).hasSize(1);
  }

  private static final class InMemory<T extends io.github.brckjr.ppmp.domain.model.BaseModel> {
    private final Map<UUID, T> store = new LinkedHashMap<>();

    Optional<T> findById(UUID id) {
      return Optional.ofNullable(store.get(id));
    }

    List<T> findAll() {
      return new ArrayList<>(store.values());
    }

    T persist(T d) {
      store.put(d.getId(), d);
      return d;
    }
  }
}
