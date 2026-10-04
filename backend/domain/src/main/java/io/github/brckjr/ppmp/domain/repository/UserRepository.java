package io.github.brckjr.ppmp.domain.repository;


import io.github.brckjr.ppmp.domain.model.shared.User;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User> {

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);
}
