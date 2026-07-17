package io.github.brckjr.ppmp.domain.service.transactions;

import io.github.brckjr.ppmp.common.enums.TransactionType;
import io.github.brckjr.ppmp.domain.model.transaction.Transaction;
import io.github.brckjr.ppmp.domain.repository.TransactionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class TransactionService {

  @Inject
  TransactionRepository repository;

  public List<Transaction> getTransactions() {
    return repository.findAll();
  }

  public List<TransactionType> getTransactionTypes() {
    return Arrays.stream(TransactionType.values()).toList();
  }

  public Transaction createTransaction(Transaction transaction) {
    return repository.persist(transaction);
  }
}
