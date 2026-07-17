package io.github.brckjr.ppmp.app.transactions;

import io.github.brckjr.ppmp.api.transactions.TransactionApi;
import io.github.brckjr.ppmp.api.transactions.dto.TransactionDto;
import io.github.brckjr.ppmp.app.transactions.mapper.TransactionDtoMapper;
import io.github.brckjr.ppmp.common.enums.TransactionType;
import io.github.brckjr.ppmp.domain.service.transactions.TransactionService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class TransactionApp implements TransactionApi {

  private final TransactionService service;
  private final TransactionDtoMapper mapper;

  @Inject
  public TransactionApp(TransactionService service, TransactionDtoMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  @Override
  public List<TransactionDto> getAllTransactions() {
    return service.getTransactions().stream().map(mapper::toDto).toList();
  }

  @Override
  public List<TransactionType> getTransactionTypes() {
    return service.getTransactionTypes();
  }

  @Override
  public TransactionDto createTransaction(TransactionDto newTransaction) {
    return mapper.toDto(service.createTransaction(mapper.toDomain(newTransaction)));
  }

}
