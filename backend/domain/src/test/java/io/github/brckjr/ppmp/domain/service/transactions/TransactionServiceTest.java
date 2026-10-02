package io.github.brckjr.ppmp.domain.service.transactions;

import io.github.brckjr.ppmp.common.enums.Currency;
import io.github.brckjr.ppmp.common.enums.TransactionType;
import io.github.brckjr.ppmp.domain.model.instrument.Instrument;
import io.github.brckjr.ppmp.domain.model.transaction.Transaction;
import io.github.brckjr.ppmp.domain.model.transaction.TransactionMetrics;
import io.github.brckjr.ppmp.domain.repository.InstrumentRepository;
import io.github.brckjr.ppmp.domain.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TransactionServiceTest {

  private InMemoryTransactionRepository repository;
  private TransactionService service;
  private InMemoryInstrumentRepository instruments;

  @BeforeEach
  void setUp() {
    repository = new InMemoryTransactionRepository();
    instruments = new InMemoryInstrumentRepository();
    service = new TransactionService(repository, instruments);
  }

  @Test
  @DisplayName("Should filter, sort, and paginate transactions correctly")
  void getAllTransactionsFiltersSortsAndPages() {
    Transaction newest = repository.persist(transaction("2026-07-30T10:00:00Z", TransactionType.BUY, "AAPL", "100.00"));
    Transaction middle = repository.persist(transaction("2026-07-29T10:00:00Z", TransactionType.SELL, "AAPL", "50.00"));
    Transaction oldest = repository.persist(transaction("2026-07-28T10:00:00Z", TransactionType.DIVIDEND, "AAPL", "10.00"));

    List<Transaction> page = service.getAllTransactions(null, 2, 1);

    assertThat(page)
      .isNotNull()
      .extracting(Transaction::getId)
      .containsExactly(middle.getId(), oldest.getId());
  }

  @Test
  @DisplayName("Should aggregate transaction metrics correctly by given time period")
  void getTransactionMetricsAggregatesByPeriod() {
    repository.persist(transaction(OffsetDateTime.now().minusMonths(3), TransactionType.BUY, "AAPL", "100.00"));
    repository.persist(transaction(OffsetDateTime.now().minusMonths(2), TransactionType.DEPOSIT, null, "250.00"));
    repository.persist(transaction(OffsetDateTime.now().minusMonths(1), TransactionType.WITHDRAWAL, null, "40.00"));
    repository.persist(transaction(OffsetDateTime.now().minusWeeks(1), TransactionType.DIVIDEND, "AAPL", "15.00"));
    repository.persist(transaction(OffsetDateTime.now().minusYears(2), TransactionType.DIVIDEND, "AAPL", "999.00"));

    TransactionMetrics metrics = service.getTransactionMetrics("1y");

    assertThat(metrics).isNotNull();
    assertThat(metrics.totalDividends()).isEqualByComparingTo("15.00");
    assertThat(metrics.netCapitalInflow()).isEqualByComparingTo("210.00");
    assertThat(metrics.totalVolume()).isEqualByComparingTo("100.00");
    assertThat(metrics.currency()).isEqualTo(Currency.USD);
  }

  @Test
  @DisplayName("Should only create transactions for already known instruments")
  void createTransactionRequiresKnownInstrument() {
    Instrument known = instruments.persist(instrument("AAPL"));
    OffsetDateTime now = OffsetDateTime.now();
    BigDecimal ten = new BigDecimal("10");

    Transaction created = service.createTransaction(now, TransactionType.BUY, known.getId(), null, ten, ten, ten, Currency.USD, null);

    assertThat(created.getInstrument()).contains(known);
    assertThat(created.getTicker()).isEqualTo("AAPL");
    assertThatThrownBy(() -> service.createTransaction(now, TransactionType.BUY, UUID.randomUUID(), null, ten, ten, ten, Currency.USD, null))
      .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> service.createTransaction(now, TransactionType.BUY, null, null, ten, ten, ten, Currency.USD, null))
      .isInstanceOf(IllegalArgumentException.class);
    assertThat(repository.count()).isEqualTo(1);
  }

  @Test
  @DisplayName("Should resolve the instrument by ticker when no id is given")
  void createTransactionResolvesTicker() {
    Instrument known = instruments.persist(instrument("AAPL"));
    BigDecimal ten = new BigDecimal("10");

    Transaction created = service.createTransaction(OffsetDateTime.now(), TransactionType.BUY, null, " aapl ", ten, ten, ten, Currency.USD, null);

    assertThat(created.getInstrument()).contains(known);
    assertThatThrownBy(() -> service.createTransaction(OffsetDateTime.now(), TransactionType.BUY, null, "NOPE", ten, ten, ten, Currency.USD, null))
      .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("Should ignore the instrument for cash transactions")
  void cashTransactionsHaveNoInstrument() {
    Instrument known = instruments.persist(instrument("AAPL"));

    Transaction created = service.createTransaction(
      OffsetDateTime.now(), TransactionType.DEPOSIT, known.getId(), null, null, null, new BigDecimal("10"), Currency.USD, null);

    assertThat(created.getInstrument()).isEmpty();
  }

  private static Instrument instrument(String ticker) {
    return Instrument.create(ticker, ticker, "USD", null, null, "US", "US", "TECHNOLOGY", "STOCK");
  }

  private static Transaction transaction(String timestamp, TransactionType type, String ticker, String grossAmount) {
    return transaction(OffsetDateTime.parse(timestamp), type, ticker, grossAmount);
  }

  private static Transaction transaction(OffsetDateTime timestamp, TransactionType type, String ticker, String grossAmount) {
    return Transaction.create(
      timestamp,
      type,
      ticker == null ? null : instrument(ticker),
      new BigDecimal("100.00"),
      new BigDecimal("1.00"),
      new BigDecimal(grossAmount),
      Currency.USD,
      null
    );
  }

  private static final class InMemoryTransactionRepository implements TransactionRepository {
    private final Map<UUID, Transaction> store = new LinkedHashMap<>();

    @Override
    public Optional<Transaction> findById(UUID id) {
      return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Transaction> findAll() {
      return new ArrayList<>(store.values());
    }

    @Override
    public Transaction persist(Transaction dto) {
      store.put(dto.getId(), dto);
      return dto;
    }

    @Override
    public Transaction update(UUID uuid, Transaction dto) {
      store.put(uuid, dto);
      return dto;
    }

    @Override
    public void deleteById(UUID id) {
      store.remove(id);
    }

    @Override
    public long count() {
      return store.size();
    }
  }

  private static final class InMemoryInstrumentRepository implements InstrumentRepository {
    private final Map<UUID, Instrument> store = new LinkedHashMap<>();

    @Override public Optional<Instrument> findById(UUID id) { return Optional.ofNullable(store.get(id)); }
    @Override public List<Instrument> findAll() { return new ArrayList<>(store.values()); }
    @Override public Instrument persist(Instrument dto) { store.put(dto.getId(), dto); return dto; }
    @Override public Instrument update(UUID uuid, Instrument dto) { store.put(uuid, dto); return dto; }
    @Override public void deleteById(UUID id) { store.remove(id); }
    @Override public long count() { return store.size(); }
    @Override public Optional<Instrument> findByTicker(String ticker) {
      return store.values().stream().filter(i -> i.getTicker().filter(ticker::equalsIgnoreCase).isPresent()).findFirst();
    }
    @Override public Optional<Instrument> findByIsin(String isin) {
      return store.values().stream().filter(i -> i.getIsin().filter(isin::equalsIgnoreCase).isPresent()).findFirst();
    }
  }
}
