package io.github.brckjr.ppmp.domain.service.auth;

/** Port for password hashing, so the domain does not depend on a concrete algorithm or library. */
public interface PasswordHasher {

  String hash(String rawPassword);

  boolean matches(String rawPassword, String hash);
}
