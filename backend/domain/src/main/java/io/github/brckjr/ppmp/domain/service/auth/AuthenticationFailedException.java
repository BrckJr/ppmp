package io.github.brckjr.ppmp.domain.service.auth;

/** Deliberately generic: it must not reveal whether the user exists or the password was wrong. */
public class AuthenticationFailedException extends RuntimeException {

  public AuthenticationFailedException() {
    super("Invalid credentials");
  }
}
