package io.github.brckjr.ppmp.app.transactions;

import io.github.brckjr.ppmp.api.transactions.TransactionApi;
import io.github.brckjr.ppmp.api.transactions.dto.TransactionDto;
import io.github.brckjr.ppmp.api.transactions.dto.TransactionMetricsDto;
import io.github.brckjr.ppmp.app.transactions.mapper.TransactionDtoMapper;
import io.github.brckjr.ppmp.common.enums.TransactionType;
import io.github.brckjr.ppmp.domain.model.transaction.TransactionMetrics;
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
  private final TransactionDtoMapper mapper;

  @Inject
  public TransactionApp(TransactionService service, TransactionDtoMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  @Override
  public List<TransactionDto> getAllTransactions(TransactionType type, int limit, int offset) {
    return service.getAllTransactions(type, limit, offset).stream().map(mapper::toDto).toList();
  }

  @Override
  public TransactionDto getTransactionById(UUID id) {
    return service.getTransactionById(id)
        .map(mapper::toDto)
        .orElseThrow(() -> new NotFoundException("Transaction not found: " + id));
  }

  @Override
  public TransactionDto createTransaction(TransactionDto newTransaction) {
    return mapper.toDto(service.createTransaction(mapper.toDomain(newTransaction)));
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
      TransactionMetrics metrics = service.getTransactionMetrics(period);
      return new TransactionMetricsDto(
          metrics.totalDividends(),
          metrics.netCapitalInflow(),
          metrics.totalVolume(),
          metrics.currency().name()
      );
    } catch (IllegalArgumentException ex) {
      throw new BadRequestException(ex.getMessage(), ex);
    }
  }

}
