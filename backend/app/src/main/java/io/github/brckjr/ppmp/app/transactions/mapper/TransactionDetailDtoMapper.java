package io.github.brckjr.ppmp.app.transactions.mapper;

import io.github.brckjr.ppmp.api.transactions.dto.TransactionDetailDto;
import io.github.brckjr.ppmp.domain.model.transaction.TransactionDetail;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface TransactionDetailDtoMapper {

    TransactionDetailDto toDto(TransactionDetail source);

    TransactionDetail toDomain(TransactionDetailDto source);
}
