package io.github.brckjr.ppmp.domain.service.transactions;

import io.github.brckjr.ppmp.common.enums.Currency;
import io.github.brckjr.ppmp.common.enums.TransactionType;
import io.github.brckjr.ppmp.domain.model.transaction.Transaction;
import io.github.brckjr.ppmp.domain.model.transaction.TransactionMetrics;
import io.github.brckjr.ppmp.domain.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionServiceTest {

  private InMemoryTransactionRepository repository;
  private TransactionService service;

  @BeforeEach
  void setUp() {
    repository = new InMemoryTransactionRepository();
    service = new TransactionService(repository);
  }

  @Test
  @DisplayName("Should filter, sort, and paginate transactions correctly")
  void getAllTransactionsFiltersSortsAndPages() {
    Transaction newest = repository.persist(transaction("2026-07-30T10:00:00Z", TransactionType.BUY, "AAPL", "100.00"));
    Transaction middle = repository.persist(transaction("2026-07-29T10:00:00Z", TransactionType.SELL, "AAPL", "50.00"));
    Transaction oldest = repository.persist(transaction("2026-07-28T10:00:00Z", TransactionType.DIVIDEND, null, "10.00"));

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

  private static Transaction transaction(String timestamp, TransactionType type, String ticker, String grossAmount) {
    return transaction(OffsetDateTime.parse(timestamp), type, ticker, grossAmount);
  }

  private static Transaction transaction(OffsetDateTime timestamp, TransactionType type, String ticker, String grossAmount) {
    return Transaction.create(
      timestamp,
      type,
      ticker,
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
}