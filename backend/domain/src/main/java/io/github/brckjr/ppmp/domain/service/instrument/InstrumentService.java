package io.github.brckjr.ppmp.domain.service.instrument;

import io.github.brckjr.ppmp.domain.model.instrument.Instrument;
import io.github.brckjr.ppmp.domain.repository.InstrumentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class InstrumentService {

  private final InstrumentRepository repository;

  @Inject
  public InstrumentService(InstrumentRepository repository) {
    this.repository = repository;
  }

  public List<Instrument> getAllInstruments(String query, String type, int limit, int offset) {
    String normalizedQuery = query == null || query.isBlank() ? null : query.trim().toLowerCase(Locale.ROOT);
    String normalizedType = type == null || type.isBlank() ? null : type.trim();

    return repository.findAll().stream()
      .filter(instrument -> normalizedType == null || instrument.getType().filter(normalizedType::equalsIgnoreCase).isPresent())
      .filter(instrument -> normalizedQuery == null || matches(instrument, normalizedQuery))
      .sorted(Comparator.comparing((Instrument instrument) -> instrument.getTicker().orElse(""), String.CASE_INSENSITIVE_ORDER)
        .thenComparing(Instrument::getId))
      .skip(Math.max(offset, 0))
      .limit(Math.max(limit, 0))
      .toList();
  }

  public Optional<Instrument> getInstrumentById(UUID id) {
    Objects.requireNonNull(id, "Instrument id cannot be null");
    return repository.findById(id);
  }

  public Optional<Instrument> getInstrumentByTicker(String ticker) {
    if (ticker == null || ticker.isBlank()) {
      return Optional.empty();
    }
    return repository.findByTicker(ticker.trim().toUpperCase(Locale.ROOT));
  }

  /** Registers a new instrument; ticker and ISIN must not already be known. */
  public Instrument createInstrument(Instrument instrument) {
    Objects.requireNonNull(instrument, "Instrument cannot be null");
    instrument.getTicker()
      .flatMap(repository::findByTicker)
      .ifPresent(existing -> {
        throw new IllegalArgumentException("Instrument with ticker already exists: " + existing.getTicker().orElse(""));
      });
    instrument.getIsin()
      .flatMap(repository::findByIsin)
      .ifPresent(existing -> {
        throw new IllegalArgumentException("Instrument with ISIN already exists: " + existing.getIsin().orElse(""));
      });
    return repository.persist(instrument);
  }

  public Instrument updateInstrument(UUID id, Instrument instrument) {
    Objects.requireNonNull(id, "Instrument id cannot be null");
    Objects.requireNonNull(instrument, "Instrument cannot be null");
    return repository.update(id, instrument);
  }

  private static boolean matches(Instrument instrument, String query) {
    return instrument.getTicker().filter(value -> value.toLowerCase(Locale.ROOT).contains(query)).isPresent()
      || instrument.getName().filter(value -> value.toLowerCase(Locale.ROOT).contains(query)).isPresent()
      || instrument.getIsin().filter(value -> value.toLowerCase(Locale.ROOT).contains(query)).isPresent();
  }
}
