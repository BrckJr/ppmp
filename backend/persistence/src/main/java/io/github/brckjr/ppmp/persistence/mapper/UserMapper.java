package io.github.brckjr.ppmp.persistence.mapper;

import io.github.brckjr.ppmp.domain.model.shared.User;
import io.github.brckjr.ppmp.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper extends BaseMapper<User, UserEntity> {

    @ObjectFactory
    default User createDomain(UserEntity entity) {
        return User.reconstitute(
                entity.getEmail(),
                entity.getUsername(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getUserStatus()
        );
    }

}
