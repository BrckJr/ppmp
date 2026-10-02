package io.github.brckjr.ppmp.persistence.repository;

import io.github.brckjr.ppmp.domain.model.transaction.Transaction;
import io.github.brckjr.ppmp.domain.repository.TransactionRepository;
import io.github.brckjr.ppmp.persistence.entity.InstrumentEntity;
import io.github.brckjr.ppmp.persistence.entity.TransactionEntity;
import io.github.brckjr.ppmp.persistence.mapper.TransactionMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TransactionRepositoryImpl extends BaseRepositoryImpl<Transaction, TransactionEntity> implements TransactionRepository {

  @Inject
  public TransactionRepositoryImpl(TransactionMapper mapper) {
    super(mapper, TransactionEntity.class);
  }

  @Override
  protected void resolveReferences(TransactionEntity entity) {
    InstrumentEntity instrument = entity.getInstrument();
    if (instrument != null) {
      entity.setInstrument(entityManager.getReference(InstrumentEntity.class, instrument.getId()));
    }
  }
}
