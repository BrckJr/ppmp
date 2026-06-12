package io.github.brckjr.ppmp.persistence.repository;

import io.github.brckjr.ppmp.domain.model.shared.User;
import io.github.brckjr.ppmp.domain.repository.UserRepository;
import io.github.brckjr.ppmp.persistence.entity.UserEntity;
import io.github.brckjr.ppmp.persistence.mapper.UserMapper;
import jakarta.inject.Inject;

public class UserRepositoryImpl extends BaseRepositoryImpl<User, UserEntity> implements UserRepository {

    private final UserMapper mapper;

    @Inject
    public UserRepositoryImpl(UserMapper mapper) {
        super(mapper, UserEntity.class);
        this.mapper = mapper;
    }
}