package io.github.brckjr.ppmp.persistence.mapper;

import io.github.brckjr.ppmp.domain.model.portfolio.Portfolio;
import io.github.brckjr.ppmp.domain.model.shared.User;
import io.github.brckjr.ppmp.persistence.entity.PortfolioEntity;
import io.github.brckjr.ppmp.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {InstrumentMapper.class})
public interface PortfolioMapper extends BaseMapper<Portfolio, PortfolioEntity> {

    // Explicit bridge method so MapStruct delegates User conversions to UserMapper
    User mapUserEntityToDomain(UserEntity entity);

    @ObjectFactory
    default Portfolio createDomain(PortfolioEntity entity) {
        return Portfolio.reconstitute(
                mapUserEntityToDomain(entity.getUserUuid()),
                entity.getName(),
                entity.getDescription(),
                entity.getBaseCurrency()
        );
    }
}
