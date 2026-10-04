package io.github.brckjr.ppmp.domain.service.portfolio;

import io.github.brckjr.ppmp.common.enums.*;
import io.github.brckjr.ppmp.common.enums.Currency;
import io.github.brckjr.ppmp.domain.model.holding.HoldingDetail;
import io.github.brckjr.ppmp.domain.model.holding.Holdings;
import io.github.brckjr.ppmp.domain.model.instrument.Instrument;
import io.github.brckjr.ppmp.domain.model.instrument.InstrumentPrice;
import io.github.brckjr.ppmp.domain.model.transaction.Transaction;
import io.github.brckjr.ppmp.domain.repository.InstrumentPriceRepository;
import io.github.brckjr.ppmp.domain.repository.InstrumentRepository;
import io.github.brckjr.ppmp.domain.repository.TransactionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class PortfolioService {

  private static final MathContext MATH_CONTEXT = MathContext.DECIMAL128;
  private final TransactionRepository transactionRepository;
  private final InstrumentRepository instrumentRepository;
  private final InstrumentPriceRepository instrumentPriceRepository;

  @Inject
  public PortfolioService(
    TransactionRepository transactionRepository,
    InstrumentRepository instrumentRepository,
    InstrumentPriceRepository instrumentPriceRepository
  ) {
    this.transactionRepository = transactionRepository;
    this.instrumentRepository = instrumentRepository;
    this.instrumentPriceRepository = instrumentPriceRepository;
  }

  public Holdings getHoldings(UUID userId) {
    return new Holdings(buildHoldings(userId));
  }

  public Optional<HoldingDetail> getHolding(UUID userId, String ticker) {
    if (ticker == null || ticker.isBlank()) {
      return Optional.empty();
    }
    String normalizedTicker = normalizeTicker(ticker);
    return buildHoldings(userId).stream()
      .filter(holding -> holding.ticker().equals(normalizedTicker))
      .findFirst();
  }

  private List<HoldingDetail> buildHoldings(UUID userId) {
    Objects.requireNonNull(userId, "User id cannot be null");
    Map<String, Instrument> instruments = instrumentRepository.findAll().stream()
      .filter(instrument -> instrument.getTicker().isPresent())
      .collect(Collectors.toMap(
        instrument -> normalizeTicker(instrument.getTicker().orElseThrow()),
        instrument -> instrument,
        (first, ignored) -> first
      ));
    Map<String, InstrumentPrice> latestPrices = new HashMap<>();
    instrumentPriceRepository.findAll().stream()
      .filter(price -> price.getInstrument().getTicker().isPresent())
      .forEach(price -> latestPrices.merge(
        normalizeTicker(price.getInstrument().getTicker().orElseThrow()),
        price,
        PortfolioService::newerPrice
      ));

    Map<String, Position> positions = new HashMap<>();
    transactionRepository.findByUserId(userId).stream()
      .filter(PortfolioService::isTrade)
      .sorted(Comparator.comparing(Transaction::getTimestamp)
        .thenComparing(Transaction::getCreatedAt)
        .thenComparing(Transaction::getId))
      .forEach(transaction -> applyTrade(positions, transaction));

    return positions.entrySet().stream()
      .filter(entry -> entry.getValue().shares.signum() > 0)
      .sorted(Map.Entry.comparingByKey())
      .map(entry -> toHolding(entry.getKey(), entry.getValue(), instruments.get(entry.getKey()), latestPrices.get(entry.getKey())))
      .toList();
  }

  private static boolean isTrade(Transaction transaction) {
    return transaction.getTransactionType() == TransactionType.BUY
      || transaction.getTransactionType() == TransactionType.SELL;
  }

  private static void applyTrade(Map<String, Position> positions, Transaction transaction) {
    String ticker = transaction.getTicker();
    if (ticker == null || ticker.isBlank()) {
      throw invalidTrade(transaction, "ticker is missing");
    }
    BigDecimal quantity = transaction.getQuantity().orElseThrow(() -> invalidTrade(transaction, "quantity is missing"));
    BigDecimal grossAmount = transaction.getGrossAmount();
    Position position = positions.computeIfAbsent(normalizeTicker(ticker), ignored -> new Position(transaction.getCurrency()));
    if (position.currency != transaction.getCurrency()) {
      throw invalidTrade(transaction, "trade currency differs from the other transactions for this ticker");
    }

    if (transaction.getTransactionType() == io.github.brckjr.ppmp.common.enums.TransactionType.BUY) {
      position.shares = position.shares.add(quantity);
      position.costBasis = position.costBasis.add(grossAmount);
      return;
    }

    if (quantity.compareTo(position.shares) > 0) {
      throw invalidTrade(transaction, "sell quantity exceeds the available long position");
    }
    BigDecimal averageCost = position.costBasis.divide(position.shares, MATH_CONTEXT);
    BigDecimal removedCost = averageCost.multiply(quantity, MATH_CONTEXT);
    position.realizedGain = position.realizedGain.add(grossAmount.subtract(removedCost));
    position.shares = position.shares.subtract(quantity);
    position.costBasis = position.costBasis.subtract(removedCost);
    if (position.shares.signum() == 0) {
      position.costBasis = BigDecimal.ZERO;
    }
  }

  private static IllegalStateException invalidTrade(Transaction transaction, String reason) {
    return new IllegalStateException("Invalid trade " + transaction.getId() + ": " + reason);
  }

  private static InstrumentPrice newerPrice(InstrumentPrice current, InstrumentPrice candidate) {
    int dateComparison = candidate.getPriceDate().compareTo(current.getPriceDate());
    if (dateComparison != 0) {
      return dateComparison > 0 ? candidate : current;
    }
    int timestampComparison = candidate.getCreatedAt().compareTo(current.getCreatedAt());
    return timestampComparison > 0 || (timestampComparison == 0 && candidate.getId().compareTo(current.getId()) > 0)
      ? candidate
      : current;
  }

  private static HoldingDetail toHolding(String ticker, Position position, Instrument instrument, InstrumentPrice latestPrice) {
    boolean priceAvailable = latestPrice != null && currencyMatches(position.currency, latestPrice.getCurrency());
    BigDecimal currentPrice = priceAvailable ? latestPrice.getClose() : null;
    BigDecimal marketValue = priceAvailable ? position.shares.multiply(currentPrice, MATH_CONTEXT) : null;
    BigDecimal unrealizedGain = priceAvailable ? marketValue.subtract(position.costBasis) : null;
    BigDecimal unrealizedGainPct = priceAvailable && position.costBasis.signum() != 0
      ? unrealizedGain.multiply(BigDecimal.valueOf(100), MATH_CONTEXT).divide(position.costBasis, MATH_CONTEXT)
      : priceAvailable ? BigDecimal.ZERO : null;
    String name = instrument == null ? ticker : instrument.getName().orElse(ticker);

    return new HoldingDetail(
      instrument == null ? UUID.nameUUIDFromBytes(ticker.getBytes(StandardCharsets.UTF_8)) : instrument.getId(),
      ticker,
      name,
      instrument == null ? null : parseEnum(Sector.class, instrument.getSector().orElse(null)),
      position.shares,
      position.costBasis.divide(position.shares, MATH_CONTEXT),
      currentPrice,
      marketValue,
      unrealizedGain,
      unrealizedGainPct,
      position.currency,
      currentPrice,
      instrument == null ? null : parseAssetClass(instrument.getType().orElse(null)),
      instrument == null ? null : parseEnum(Region.class, instrument.getRegion().orElse(null)),
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      marketValue,
      position.costBasis,
      unrealizedGain,
      unrealizedGainPct,
      position.realizedGain,
      priceAvailable
    );
  }

  private static boolean currencyMatches(Currency currency, String quoteCurrency) {
    return currency != null && (currency.name().equalsIgnoreCase(quoteCurrency)
      || (currency == Currency.YEN && "JPY".equalsIgnoreCase(quoteCurrency)));
  }

  private static AssetClass parseAssetClass(String value) {
    if (value == null) {
      return null;
    }
    String normalized = value.trim().toUpperCase(Locale.ROOT);
    if (normalized.equals("STOCK") || normalized.equals("EQUITY")) {
      normalized = "STOCKS";
    } else if (normalized.equals("ETF")) {
      normalized = "ETFS";
    } else if (normalized.equals("DERIVATIVE")) {
      normalized = "DERIVATIVES";
    }
    return parseEnum(AssetClass.class, normalized);
  }

  private static <E extends Enum<E>> E parseEnum(Class<E> enumType, String value) {
    if (value == null) {
      return null;
    }
    try {
      return Enum.valueOf(enumType, value.trim().replace(' ', '_').replace('-', '_').toUpperCase(Locale.ROOT));
    } catch (IllegalArgumentException exception) {
      return null;
    }
  }

  private static String normalizeTicker(String ticker) {
    return ticker.trim().toUpperCase(Locale.ROOT);
  }

  private static final class Position {
    private final Currency currency;
    private BigDecimal shares = BigDecimal.ZERO;
    private BigDecimal costBasis = BigDecimal.ZERO;
    private BigDecimal realizedGain = BigDecimal.ZERO;

    private Position(Currency currency) {
      this.currency = currency;
    }
  }
}
