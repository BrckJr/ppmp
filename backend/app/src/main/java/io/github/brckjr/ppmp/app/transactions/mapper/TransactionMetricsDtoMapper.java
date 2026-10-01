package io.github.brckjr.ppmp.app.transactions.mapper;

import io.github.brckjr.ppmp.api.transactions.dto.TransactionMetricsDto;
import io.github.brckjr.ppmp.domain.model.transaction.TransactionMetrics;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface TransactionMetricsDtoMapper {
  default TransactionMetricsDto toDto(TransactionMetrics metrics) {
    return new TransactionMetricsDto(
      metrics.totalDividends(),
      metrics.netCapitalInflow(),
      metrics.totalVolume(),
      metrics.currency().name()
    );
  }

  TransactionMetrics toDomain(TransactionMetricsDto transactionMetricsDto);
}
