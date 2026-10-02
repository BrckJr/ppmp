package io.github.brckjr.ppmp.domain.service.instrument;

import io.github.brckjr.ppmp.domain.model.instrument.Instrument;
import io.github.brckjr.ppmp.domain.repository.InstrumentRepository;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InstrumentServiceTest {

  private final Map<UUID, Instrument> store = new LinkedHashMap<>();
  private final InstrumentService service = new InstrumentService(new InstrumentRepository() {
    @Override public Optional<Instrument> findById(UUID id) { return Optional.ofNullable(store.get(id)); }
    @Override public List<Instrument> findAll() { return new ArrayList<>(store.values()); }
    @Override public Instrument persist(Instrument d) { store.put(d.getId(), d); return d; }
    @Override public Instrument update(UUID id, Instrument d) { store.put(id, d); return d; }
    @Override public void deleteById(UUID id) { store.remove(id); }
    @Override public long count() { return store.size(); }
    @Override public Optional<Instrument> findByTicker(String t) {
      return store.values().stream().filter(i -> i.getTicker().filter(t::equalsIgnoreCase).isPresent()).findFirst();
    }
    @Override public Optional<Instrument> findByIsin(String isin) {
      return store.values().stream().filter(i -> i.getIsin().filter(isin::equalsIgnoreCase).isPresent()).findFirst();
    }
  });

  @Test
  void createsInstrumentWithNormalizedTicker() {
    Instrument created = service.createInstrument(
      Instrument.create("Apple Inc.", " aapl ", "usd", "us0378331005", "NASDAQ", "US", "US", "TECHNOLOGY", "STOCK"));

    assertThat(created.getTicker()).contains("AAPL");
    assertThat(created.getCurrency()).isEqualTo("USD");
    assertThat(service.getInstrumentByTicker("aapl")).contains(created);
  }

  @Test
  void rejectsDuplicateTickerAndIsin() {
    service.createInstrument(Instrument.create("Apple", "AAPL", "USD", "US0378331005", null, null, null, null, "STOCK"));

    assertThatThrownBy(() -> service.createInstrument(Instrument.create("Other", "aapl", "USD", null, null, null, null, null, "STOCK")))
      .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> service.createInstrument(Instrument.create("Other", "OTHR", "USD", "US0378331005", null, null, null, null, "STOCK")))
      .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void rejectsMissingTickerOrType() {
    assertThatThrownBy(() -> Instrument.create("X", " ", "USD", null, null, null, null, null, "STOCK"))
      .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> Instrument.create("X", "X", "USD", null, null, null, null, null, null))
      .isInstanceOf(IllegalArgumentException.class);
  }
}
