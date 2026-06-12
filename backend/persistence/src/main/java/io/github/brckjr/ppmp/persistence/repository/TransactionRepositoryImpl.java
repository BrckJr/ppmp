package io.github.brckjr.ppmp.persistence.repository;

import io.github.brckjr.ppmp.domain.model.transaction.Transaction;
import io.github.brckjr.ppmp.domain.repository.TransactionRepository;
import io.github.brckjr.ppmp.persistence.entity.TransactionEntity;
import io.github.brckjr.ppmp.persistence.mapper.TransactionMapper;
import jakarta.inject.Inject;

public class TransactionRepositoryImpl extends BaseRepositoryImpl<Transaction, TransactionEntity> implements TransactionRepository {

    private final TransactionMapper mapper;

    @Inject
    public TransactionRepositoryImpl(TransactionMapper mapper) {
        super(mapper, TransactionEntity.class);
        this.mapper = mapper;
    }
}
