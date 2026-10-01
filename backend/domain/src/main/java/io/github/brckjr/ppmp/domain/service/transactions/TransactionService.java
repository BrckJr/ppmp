package io.github.brckjr.ppmp.domain.service.transactions;

import io.github.brckjr.ppmp.common.enums.Currency;
import io.github.brckjr.ppmp.common.enums.TransactionType;
import io.github.brckjr.ppmp.domain.model.transaction.Transaction;
import io.github.brckjr.ppmp.domain.model.transaction.TransactionMetrics;
import io.github.brckjr.ppmp.domain.repository.TransactionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Function;

@ApplicationScoped
public class TransactionService {

  private final TransactionRepository repository;

  @Inject
  public TransactionService(TransactionRepository repository) {
    this.repository = repository;
  }

  public List<Transaction> getAllTransactions(TransactionType type, int limit, int offset) {
    int normalizedLimit = Math.max(limit, 0);
    int normalizedOffset = Math.max(offset, 0);

    return repository.findAll().stream()
      .sorted(Comparator
        .comparing(Transaction::getTimestamp)
        .reversed()
        .thenComparing(Transaction::getCreatedAt, Comparator.reverseOrder())
        .thenComparing(Transaction::getId, Comparator.reverseOrder()))
      .filter(transaction -> type == null || transaction.getTransactionType() == type)
      .skip(normalizedOffset)
      .limit(normalizedLimit)
      .toList();
  }

  public Optional<Transaction> getTransactionById(UUID id) {
    Objects.requireNonNull(id, "Transaction id cannot be null");
    return repository.findById(id);
  }

  public Transaction createTransaction(Transaction transaction) {
    return repository.persist(transaction);
  }

  public boolean deleteTransaction(UUID id) {
    Objects.requireNonNull(id, "Transaction id cannot be null");

    if (repository.findById(id).isEmpty()) {
      return false;
    }

    repository.deleteById(id);
    return true;
  }

  public TransactionMetrics getTransactionMetrics(String period) {
    List<Transaction> transactions = transactionsForPeriod(period);
    Currency currency = transactions.stream()
      .max(Comparator
        .comparing(Transaction::getTimestamp)
        .thenComparing(Transaction::getCreatedAt)
        .thenComparing(Transaction::getId))
      .map(Transaction::getCurrency)
      .orElse(Currency.EUR);

    BigDecimal totalDividends = sum(transactions, transaction ->
      transaction.getTransactionType() == TransactionType.DIVIDEND ? transaction.getGrossAmount() : BigDecimal.ZERO);
    BigDecimal netCapitalInflow = sum(transactions, transaction -> switch (transaction.getTransactionType()) {
      case DEPOSIT -> transaction.getGrossAmount();
      case WITHDRAWAL -> transaction.getGrossAmount().negate();
      default -> BigDecimal.ZERO;
    });
    BigDecimal totalVolume = sum(transactions, transaction ->
      transaction.getTransactionType() == TransactionType.BUY || transaction.getTransactionType() == TransactionType.SELL
        ? transaction.getGrossAmount().abs()
        : BigDecimal.ZERO);

    return new TransactionMetrics(totalDividends, netCapitalInflow, totalVolume, currency);
  }

  private List<Transaction> transactionsForPeriod(String period) {
    OffsetDateTime start = resolvePeriodStart(period);
    return repository.findAll().stream()
      .filter(transaction -> start == null || !transaction.getTimestamp().isBefore(start))
      .toList();
  }

  private OffsetDateTime resolvePeriodStart(String period) {
    String normalized = period == null ? "ytd" : period.trim().toLowerCase();
    OffsetDateTime now = OffsetDateTime.now();

    return switch (normalized) {
      case "all" -> null;
      case "1y" -> now.minusYears(1);
      case "ytd" -> now.with(TemporalAdjusters.firstDayOfYear()).toLocalDate().atStartOfDay().atOffset(now.getOffset());
      default -> throw new IllegalArgumentException("Unsupported transaction metrics period: " + period);
    };
  }

  private BigDecimal sum(List<Transaction> transactions, Function<Transaction, BigDecimal> extractor) {
    return transactions.stream()
      .map(extractor)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
