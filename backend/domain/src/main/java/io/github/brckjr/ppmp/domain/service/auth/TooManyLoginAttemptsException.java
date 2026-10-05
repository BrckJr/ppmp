package io.github.brckjr.ppmp.domain.service.auth;

public class TooManyLoginAttemptsException extends RuntimeException {

  public TooManyLoginAttemptsException() {
    super("Too many failed login attempts. Please try again later.");
  }
}
