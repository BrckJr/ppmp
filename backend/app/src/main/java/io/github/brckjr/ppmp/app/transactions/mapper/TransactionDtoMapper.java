package io.github.brckjr.ppmp.app.transactions.mapper;

import io.github.brckjr.ppmp.api.transactions.dto.TransactionDto;
import io.github.brckjr.ppmp.domain.model.transaction.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface TransactionDtoMapper {

  default Transaction toDomain(TransactionDto dto) {
    if (dto == null) {
      return null;
    }

    return Transaction.create(
      dto.timestamp(),
      dto.type(),
      dto.ticker(),
      dto.unitPrice(),
      dto.quantity(),
      dto.grossAmount(),
      dto.currency(),
      dto.comment()
    );
  }

  default TransactionDto toDto(Transaction domain) {

    return new TransactionDto(
      domain.getId(),
      domain.getTimestamp(),
      domain.getTransactionType(),
      domain.getTicker(),
      domain.getQuantity().orElse(null),
      domain.getUnitPrice().orElse(null),
      domain.getGrossAmount(),
      domain.getCurrency(),
      domain.getComment().orElse(null)
    );
  }
}
