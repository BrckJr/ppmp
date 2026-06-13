package io.github.brckjr.ppmp.persistence.mapper;

import io.github.brckjr.ppmp.domain.model.portfolio.Portfolio;
import io.github.brckjr.ppmp.domain.model.shared.User;
import io.github.brckjr.ppmp.persistence.entity.PortfolioEntity;
import io.github.brckjr.ppmp.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface PortfolioMapper extends BaseMapper<Portfolio, PortfolioEntity> {

    default User toUser(UserEntity entity) {
        return new UserMapper() {
        }.toModel(entity);
    }

    default UserEntity toUserEntity(User user) {
        return new UserMapper() {
        }.toEntity(user);
    }

    @Override
    default Portfolio toModel(PortfolioEntity entity) {
        if (entity == null) {
            return null;
        }
        return Portfolio.reconstitute(
                toUser(entity.getUserUuid()),
                entity.getName(),
                entity.getDescription(),
                entity.getBaseCurrency()
        );
    }

    @Override
    default PortfolioEntity toEntity(Portfolio model) {
        if (model == null) {
            return null;
        }
        PortfolioEntity entity = new PortfolioEntity();
        entity.setId(model.getId());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setUserUuid(toUserEntity(model.getUser()));
        entity.setName(model.getName());
        entity.setDescription(model.getDescription().orElse(null));
        entity.setBaseCurrency(model.getBaseCurrency());
        return entity;
    }

    @Override
    default void updateEntityFromModel(Portfolio model, @MappingTarget PortfolioEntity entity) {
        if (model == null || entity == null) {
            return;
        }
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setUserUuid(toUserEntity(model.getUser()));
        entity.setName(model.getName());
        entity.setDescription(model.getDescription().orElse(null));
        entity.setBaseCurrency(model.getBaseCurrency());
    }
}
