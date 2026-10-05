package io.github.brckjr.ppmp.persistence.mapper;

import io.github.brckjr.ppmp.domain.model.shared.User;
import io.github.brckjr.ppmp.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface UserMapper extends BaseMapper<User, UserEntity> {

    @Override
    default User toModel(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return User.reconstitute(
                entity.getId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getEmail(),
                entity.getUsername(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getUserStatus(),
                entity.getPasswordHash()
        );
    }

    @Override
    default UserEntity toEntity(User model) {
        if (model == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setId(model.getId());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setEmail(model.getEmail());
        entity.setUsername(model.getUsername());
        entity.setFirstName(model.getFirstName().orElse(null));
        entity.setLastName(model.getLastName().orElse(null));
        entity.setUserStatus(model.getUserStatus().orElse(null));
        entity.setPasswordHash(model.getPasswordHash().orElse(null));
        return entity;
    }

    @Override
    default void updateEntityFromModel(User model, @MappingTarget UserEntity entity) {
        if (model == null || entity == null) {
            return;
        }
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setEmail(model.getEmail());
        entity.setUsername(model.getUsername());
        entity.setFirstName(model.getFirstName().orElse(null));
        entity.setLastName(model.getLastName().orElse(null));
        entity.setUserStatus(model.getUserStatus().orElse(null));
        entity.setPasswordHash(model.getPasswordHash().orElse(null));
    }
}
