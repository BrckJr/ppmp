package io.github.brckjr.ppmp.app.transactions;

import io.github.brckjr.ppmp.api.transactions.TransactionApi;
import io.github.brckjr.ppmp.api.transactions.dto.TransactionDto;
import io.github.brckjr.ppmp.api.transactions.dto.TransactionMetricsDto;
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

  @Inject
  public TransactionApp(TransactionService service, TransactionDtoMapper transactionDtoMapper, TransactionMetricsDtoMapper transactionMetricsDtoMapper) {
    this.service = service;
    this.transactionDtoMapper = transactionDtoMapper;
    this.transactionMetricsDtoMapper = transactionMetricsDtoMapper;
  }

  @Override
  public List<TransactionDto> getAllTransactions(TransactionType type, int limit, int offset) {
    return service.getAllTransactions(type, limit, offset).stream().map(transactionDtoMapper::toDto).toList();
  }

  @Override
  public TransactionDto getTransactionById(UUID id) {
    return service.getTransactionById(id)
      .map(transactionDtoMapper::toDto)
      .orElseThrow(() -> new NotFoundException("Transaction not found: " + id));
  }

  @Override
  public TransactionDto createTransaction(TransactionDto newTransaction) {
    return transactionDtoMapper.toDto(service.createTransaction(transactionDtoMapper.toDomain(newTransaction)));
  }

  @Override
  public void deleteTransaction(UUID id) {
    if (!service.deleteTransaction(id)) {
      throw new NotFoundException("Transaction not found: " + id);
    }
  }

  @Override
  public TransactionMetricsDto getTransactionMetrics(String period) {
    try {
      return transactionMetricsDtoMapper.toDto(service.getTransactionMetrics(period));

    } catch (IllegalArgumentException ex) {
      throw new BadRequestException(ex.getMessage(), ex);
    }
  }

}
