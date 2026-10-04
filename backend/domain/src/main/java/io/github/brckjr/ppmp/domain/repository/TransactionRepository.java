package io.github.brckjr.ppmp.domain.repository;


import io.github.brckjr.ppmp.domain.model.transaction.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends BaseRepository<Transaction> {

  List<Transaction> findByUserId(UUID userId);

  /** Only returns the transaction if it belongs to the given user. */
  Optional<Transaction> findByIdAndUserId(UUID id, UUID userId);
}
