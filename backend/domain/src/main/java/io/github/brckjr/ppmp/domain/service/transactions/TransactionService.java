package io.github.brckjr.ppmp.domain.service.transactions;

import io.github.brckjr.ppmp.domain.model.transaction.Transaction;
import io.github.brckjr.ppmp.domain.repository.TransactionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class TransactionService {

  @Inject
  TransactionRepository repository;

  public List<Transaction> getAllTransactions() {
    return repository.findAll();
  }

  public Transaction createTransaction(Transaction transaction) {
    return repository.persist(transaction);
  }
}
