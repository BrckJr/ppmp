package io.github.brckjr.ppmp.persistence.repository;

import io.github.brckjr.ppmp.domain.model.shared.User;
import io.github.brckjr.ppmp.domain.repository.UserRepository;
import io.github.brckjr.ppmp.persistence.entity.UserEntity;
import io.github.brckjr.ppmp.persistence.mapper.UserMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class UserRepositoryImpl extends BaseRepositoryImpl<User, UserEntity> implements UserRepository {

    @Inject
    public UserRepositoryImpl(UserMapper mapper) {
        super(mapper, UserEntity.class);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaStreamer.stream(UserEntity.class)
            .filter(entity -> entity.getUsername().equals(username))
            .findFirst()
            .map(mapper::toModel);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaStreamer.stream(UserEntity.class)
            .filter(entity -> entity.getEmail().equals(email))
            .findFirst()
            .map(mapper::toModel);
    }
}
