package io.github.brckjr.ppmp.app.auth;

import io.github.brckjr.ppmp.domain.service.auth.PasswordHasher;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BcryptPasswordHasher implements PasswordHasher {

  private static final int ITERATION_COUNT = 12;

  @Override
  public String hash(String rawPassword) {
    return BcryptUtil.bcryptHash(rawPassword, ITERATION_COUNT);
  }

  @Override
  public boolean matches(String rawPassword, String hash) {
    return BcryptUtil.matches(rawPassword, hash);
  }
}
