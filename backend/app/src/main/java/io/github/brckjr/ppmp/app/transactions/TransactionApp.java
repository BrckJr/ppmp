package io.github.brckjr.ppmp.app.transactions;

import io.github.brckjr.ppmp.api.transactions.TransactionApi;
import io.github.brckjr.ppmp.api.transactions.dto.TransactionDto;
import io.github.brckjr.ppmp.api.transactions.dto.TransactionMetricsDto;
import io.github.brckjr.ppmp.app.auth.CurrentUser;
import io.github.brckjr.ppmp.app.transactions.mapper.TransactionDtoMapper;
import io.github.brckjr.ppmp.app.transactions.mapper.TransactionMetricsDtoMapper;
import io.github.brckjr.ppmp.common.enums.TransactionType;
import io.github.brckjr.ppmp.domain.service.transactions.TransactionService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class TransactionApp implements TransactionApi {

  private final TransactionService service;
  private final TransactionDtoMapper transactionDtoMapper;
  private final TransactionMetricsDtoMapper transactionMetricsDtoMapper;
  private final CurrentUser currentUser;

  @Inject
  public TransactionApp(TransactionService service, TransactionDtoMapper transactionDtoMapper, TransactionMetricsDtoMapper transactionMetricsDtoMapper, CurrentUser currentUser) {
    this.service = service;
    this.transactionDtoMapper = transactionDtoMapper;
    this.transactionMetricsDtoMapper = transactionMetricsDtoMapper;
    this.currentUser = currentUser;
  }

  @Override
  public List<TransactionDto> getAllTransactions(TransactionType type, int limit, int offset) {
    return service.getAllTransactions(currentUser.id(), type, limit, offset).stream().map(transactionDtoMapper::toDto).toList();
  }

  @Override
  public TransactionDto getTransactionById(UUID id) {
    return service.getTransactionById(currentUser.id(), id)
      .map(transactionDtoMapper::toDto)
      .orElseThrow(() -> new NotFoundException("Transaction not found: " + id));
  }

  @Override
  public TransactionDto createTransaction(TransactionDto newTransaction) {
    try {
      return transactionDtoMapper.toDto(service.createTransaction(
        currentUser.id(),
        newTransaction.timestamp(),
        newTransaction.type(),
        newTransaction.instrumentId(),
        newTransaction.ticker(),
        newTransaction.unitPrice(),
        newTransaction.quantity(),
        newTransaction.grossAmount(),
        newTransaction.currency(),
        newTransaction.comment()
      ));
    } catch (IllegalArgumentException | NullPointerException ex) {
      throw new BadRequestException(ex.getMessage(), ex);
    }
  }

  @Override
  public void deleteTransaction(UUID id) {
    if (!service.deleteTransaction(currentUser.id(), id)) {
      throw new NotFoundException("Transaction not found: " + id);
    }
  }

  @Override
  public TransactionMetricsDto getTransactionMetrics(String period) {
    try {
      return transactionMetricsDtoMapper.toDto(service.getTransactionMetrics(currentUser.id(), period));

    } catch (IllegalArgumentException ex) {
      throw new BadRequestException(ex.getMessage(), ex);
    }
  }

}
