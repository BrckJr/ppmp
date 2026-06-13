package io.github.brckjr.ppmp.persistence.mapper;

import io.github.brckjr.ppmp.domain.model.transaction.Transaction;
import io.github.brckjr.ppmp.persistence.entity.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface TransactionMapper extends BaseMapper<Transaction, TransactionEntity> {

    private PortfolioMapper portfolioMapper() {
        return new PortfolioMapper() {
        };
    }

    private InstrumentMapper instrumentMapper() {
        return new InstrumentMapper() {
        };
    }

    @Override
    default Transaction toModel(TransactionEntity entity) {
        if (entity == null) {
            return null;
        }
        return Transaction.reconstitute(
                portfolioMapper().toModel(entity.getPortfolio()),
                entity.getInstrument() != null ? instrumentMapper().toModel(entity.getInstrument()) : null,
                entity.getTransactionType(),
                entity.getTimestamp(),
                entity.getUnitPrice(),
                entity.getQuantity(),
                entity.getGrossAmount(),
                entity.getCurrency(),
                entity.getComment()
        );
    }

    @Override
    default TransactionEntity toEntity(Transaction model) {
        if (model == null) {
            return null;
        }
        TransactionEntity entity = new TransactionEntity();
        entity.setId(model.getId());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setPortfolio(portfolioMapper().toEntity(model.getPortfolio()));
        entity.setInstrument(model.getInstrument().map(instrumentMapper()::toEntity).orElse(null));
        entity.setTransactionType(model.getTransactionType().orElse(null));
        entity.setTimestamp(model.getTimestamp());
        entity.setUnitPrice(model.getUnitPrice().orElse(null));
        entity.setQuantity(model.getQuantity().orElse(null));
        entity.setGrossAmount(model.getGrossAmount());
        entity.setCurrency(model.getCurrency());
        entity.setComment(model.getComment().orElse(null));
        return entity;
    }

    @Override
    default void updateEntityFromModel(Transaction model, @MappingTarget TransactionEntity entity) {
        if (model == null || entity == null) {
            return;
        }
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setPortfolio(portfolioMapper().toEntity(model.getPortfolio()));
        entity.setInstrument(model.getInstrument().map(instrumentMapper()::toEntity).orElse(null));
        entity.setTransactionType(model.getTransactionType().orElse(null));
        entity.setTimestamp(model.getTimestamp());
        entity.setUnitPrice(model.getUnitPrice().orElse(null));
        entity.setQuantity(model.getQuantity().orElse(null));
        entity.setGrossAmount(model.getGrossAmount());
        entity.setCurrency(model.getCurrency());
        entity.setComment(model.getComment().orElse(null));
    }
}
