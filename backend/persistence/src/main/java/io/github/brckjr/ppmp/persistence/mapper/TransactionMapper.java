package io.github.brckjr.ppmp.persistence.mapper;

import io.github.brckjr.ppmp.domain.model.instrument.Instrument;
import io.github.brckjr.ppmp.domain.model.portfolio.Portfolio;
import io.github.brckjr.ppmp.domain.model.transaction.Transaction;
import io.github.brckjr.ppmp.persistence.entity.InstrumentEntity;
import io.github.brckjr.ppmp.persistence.entity.PortfolioEntity;
import io.github.brckjr.ppmp.persistence.entity.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {InstrumentMapper.class})
public interface TransactionMapper extends BaseMapper<Transaction, TransactionEntity> {

    // Explicit bridges for nested entity-to-domain delegation
    Portfolio mapPortfolioEntityToDomain(PortfolioEntity entity);

    Instrument mapInstrumentEntityToDomain(InstrumentEntity entity);

    @ObjectFactory
    default Transaction createDomain(TransactionEntity entity) {
        return Transaction.reconstitute(
                mapPortfolioEntityToDomain(entity.getPortfolio()),
                entity.getInstrument() != null ? mapInstrumentEntityToDomain(entity.getInstrument()) : null,
                entity.getTransactionType(),
                entity.getTimestamp(),
                entity.getUnitPrice(),
                entity.getQuantity(),
                entity.getGrossAmount(),
                entity.getCurrency(),
                entity.getComment()
        );
    }
}
