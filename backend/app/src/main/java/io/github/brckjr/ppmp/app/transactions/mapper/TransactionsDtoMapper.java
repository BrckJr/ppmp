package io.github.brckjr.ppmp.app.transactions.mapper;

import io.github.brckjr.ppmp.api.transactions.dto.TransactionsDto;
import io.github.brckjr.ppmp.domain.model.transaction.Transactions;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = TransactionDetailDtoMapper.class)
public interface TransactionsDtoMapper {

    TransactionsDto toDto(Transactions source);

    Transactions toDomain(TransactionsDto source);
}
