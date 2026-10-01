package io.github.brckjr.ppmp.domain.service.portfolio;

import io.github.brckjr.ppmp.common.enums.Currency;
import io.github.brckjr.ppmp.common.enums.TransactionType;
import io.github.brckjr.ppmp.domain.model.holding.HoldingDetail;
import io.github.brckjr.ppmp.domain.model.instrument.Instrument;
import io.github.brckjr.ppmp.domain.model.instrument.InstrumentPrice;
import io.github.brckjr.ppmp.domain.model.transaction.Transaction;
import io.github.brckjr.ppmp.domain.repository.InstrumentPriceRepository;
import io.github.brckjr.ppmp.domain.repository.InstrumentRepository;
import io.github.brckjr.ppmp.domain.repository.TransactionRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PortfolioServiceTest {

  @Test
  void derivesOpenPositionAndPerformanceFromBuysSellsAndLatestQuote() {
    InMemoryTransactionRepository transactions = new InMemoryTransactionRepository();
    InMemoryInstrumentRepository instruments = new InMemoryInstrumentRepository();
    InMemoryInstrumentPriceRepository prices = new InMemoryInstrumentPriceRepository();
    Instrument instrument = Instrument.create("Example Corp", "EXM", "USD", null, null, "US", "US", "TECHNOLOGY", "STOCK");
    instruments.persist(instrument);
    transactions.persist(transaction("2026-01-01T00:00:00Z", TransactionType.BUY, "EXM", "10", "1000"));
    transactions.persist(transaction("2026-01-02T00:00:00Z", TransactionType.SELL, "EXM", "4", "500"));
    transactions.persist(transaction("2026-01-03T00:00:00Z", TransactionType.BUY, "EXM", "2", "300"));
    prices.persist(InstrumentPrice.create(instrument, LocalDate.parse("2026-01-04"), decimal("120"), decimal("130"), decimal("110"), decimal("125"), null, 100L, "USD", "test"));
    prices.persist(InstrumentPrice.create(instrument, LocalDate.parse("2026-01-03"), decimal("980"), decimal("990"), decimal("970"), decimal("985"), null, 100L, "USD", "late-import"));

    PortfolioService service = new PortfolioService(transactions, instruments, prices);
    HoldingDetail holding = service.getHolding("exm").orElseThrow();

    assertThat(service.getHoldings().holdings()).containsExactly(holding);
    assertThat(holding.shares()).isEqualByComparingTo("8");
    assertThat(holding.costBasis()).isEqualByComparingTo("900");
    assertThat(holding.avgCost()).isEqualByComparingTo("112.5");
    assertThat(holding.marketValue()).isEqualByComparingTo("1000");
    assertThat(holding.unrealizedGain()).isEqualByComparingTo("100");
    assertThat(holding.unrealizedGainPct()).isEqualByComparingTo("11.11111111111111111111111111111111");
    assertThat(holding.realizedGain()).isEqualByComparingTo("100");
    assertThat(holding.priceAvailable()).isTrue();
  }

  @Test
  void omitsClosedPositionsAndMarksQuoteUnavailableWhenNoPriceExists() {
    InMemoryTransactionRepository transactions = new InMemoryTransactionRepository();
    transactions.persist(transaction("2026-01-01T00:00:00Z", TransactionType.BUY, "OPEN", "2", "20"));
    transactions.persist(transaction("2026-01-02T00:00:00Z", TransactionType.SELL, "CLOSED", "1", "15"));
    transactions.persist(transaction("2026-01-01T00:00:00Z", TransactionType.BUY, "CLOSED", "1", "10"));

    PortfolioService service = new PortfolioService(
        transactions,
        new InMemoryInstrumentRepository(),
        new InMemoryInstrumentPriceRepository()
    );

    assertThat(service.getHoldings().holdings()).hasSize(1);
    HoldingDetail holding = service.getHolding("OPEN").orElseThrow();
    assertThat(holding.priceAvailable()).isFalse();
    assertThat(holding.price()).isNull();
    assertThat(holding.marketValue()).isNull();
    assertThat(holding.costBasis()).isEqualByComparingTo("20");
    assertThat(service.getHolding("CLOSED")).isEmpty();
  }

  private static Transaction transaction(String timestamp, TransactionType type, String ticker, String quantity, String grossAmount) {
    return Transaction.create(
        OffsetDateTime.parse(timestamp),
        type,
        ticker,
        decimal("100"),
        decimal(quantity),
        decimal(grossAmount),
        Currency.USD,
        null
    );
  }

  private static BigDecimal decimal(String value) {
    return new BigDecimal(value);
  }

  private abstract static class InMemoryRepository<T> {
    private final Map<UUID, T> values = new LinkedHashMap<>();

    protected abstract UUID id(T value);

    List<T> findAll() {
      return new ArrayList<>(values.values());
    }

    Optional<T> findById(UUID id) {
      return Optional.ofNullable(values.get(id));
    }

    T persist(T value) {
      values.put(id(value), value);
      return value;
    }

    T update(UUID id, T value) {
      values.put(id, value);
      return value;
    }

    void deleteById(UUID id) {
      values.remove(id);
    }

    long count() {
      return values.size();
    }
  }

  private static final class InMemoryTransactionRepository extends InMemoryRepository<Transaction> implements TransactionRepository {
    @Override protected UUID id(Transaction value) { return value.getId(); }
    @Override public List<Transaction> findAll() { return super.findAll(); }
    @Override public Optional<Transaction> findById(UUID id) { return super.findById(id); }
    @Override public Transaction persist(Transaction value) { return super.persist(value); }
    @Override public Transaction update(UUID id, Transaction value) { return super.update(id, value); }
    @Override public void deleteById(UUID id) { super.deleteById(id); }
    @Override public long count() { return super.count(); }
  }

  private static final class InMemoryInstrumentRepository extends InMemoryRepository<Instrument> implements InstrumentRepository {
    @Override protected UUID id(Instrument value) { return value.getId(); }
    @Override public List<Instrument> findAll() { return super.findAll(); }
    @Override public Optional<Instrument> findById(UUID id) { return super.findById(id); }
    @Override public Instrument persist(Instrument value) { return super.persist(value); }
    @Override public Instrument update(UUID id, Instrument value) { return super.update(id, value); }
    @Override public void deleteById(UUID id) { super.deleteById(id); }
    @Override public long count() { return super.count(); }
  }

  private static final class InMemoryInstrumentPriceRepository extends InMemoryRepository<InstrumentPrice> implements InstrumentPriceRepository {
    @Override protected UUID id(InstrumentPrice value) { return value.getId(); }
    @Override public List<InstrumentPrice> findAll() { return super.findAll(); }
    @Override public Optional<InstrumentPrice> findById(UUID id) { return super.findById(id); }
    @Override public InstrumentPrice persist(InstrumentPrice value) { return super.persist(value); }
    @Override public InstrumentPrice update(UUID id, InstrumentPrice value) { return super.update(id, value); }
    @Override public void deleteById(UUID id) { super.deleteById(id); }
    @Override public long count() { return super.count(); }
  }
}