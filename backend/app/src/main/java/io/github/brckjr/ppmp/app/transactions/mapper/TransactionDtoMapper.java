package io.github.brckjr.ppmp.app.transactions.mapper;

import io.github.brckjr.ppmp.api.transactions.dto.TransactionDto;
import io.github.brckjr.ppmp.domain.model.instrument.Instrument;
import io.github.brckjr.ppmp.domain.model.transaction.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface TransactionDtoMapper {

  default TransactionDto toDto(Transaction domain) {

    return new TransactionDto(
      domain.getId(),
      domain.getTimestamp(),
      domain.getTransactionType(),
      domain.getInstrument().map(Instrument::getId).orElse(null),
      domain.getTicker(),
      domain.getInstrument().flatMap(Instrument::getName).orElse(null),
      domain.getQuantity().orElse(null),
      domain.getUnitPrice().orElse(null),
      domain.getGrossAmount(),
      domain.getCurrency(),
      domain.getComment().orElse(null)
    );
  }
}
