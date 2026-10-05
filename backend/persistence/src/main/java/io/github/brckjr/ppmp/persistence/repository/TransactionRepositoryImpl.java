package io.github.brckjr.ppmp.persistence.repository;

import io.github.brckjr.ppmp.domain.model.transaction.Transaction;
import io.github.brckjr.ppmp.domain.repository.TransactionRepository;
import io.github.brckjr.ppmp.persistence.entity.InstrumentEntity;
import io.github.brckjr.ppmp.persistence.entity.TransactionEntity;
import io.github.brckjr.ppmp.persistence.mapper.TransactionMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class TransactionRepositoryImpl extends BaseRepositoryImpl<Transaction, TransactionEntity> implements TransactionRepository {

  @Inject
  public TransactionRepositoryImpl(TransactionMapper mapper) {
    super(mapper, TransactionEntity.class);
  }

  @Override
  public List<Transaction> findByUserId(UUID userId) {
    return jpaStreamer.stream(TransactionEntity.class)
      .filter(entity -> userId.equals(entity.getUserId()))
      .map(mapper::toModel)
      .toList();
  }

  @Override
  public Optional<Transaction> findByIdAndUserId(UUID id, UUID userId) {
    return jpaStreamer.stream(TransactionEntity.class)
      .filter(entity -> entity.getId().equals(id) && userId.equals(entity.getUserId()))
      .findFirst()
      .map(mapper::toModel);
  }

  @Override
  protected void resolveReferences(TransactionEntity entity) {
    InstrumentEntity instrument = entity.getInstrument();
    if (instrument != null) {
      entity.setInstrument(entityManager.getReference(InstrumentEntity.class, instrument.getId()));
    }
  }
}
