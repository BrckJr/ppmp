package io.github.brckjr.ppmp.app.auth;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.UUID;

/**
 * The only source of the acting user's id. It comes from the verified token and never from
 * request parameters or bodies, so a client cannot choose whose data it accesses.
 */
@RequestScoped
public class CurrentUser {

  private final JsonWebToken jwt;

  @Inject
  public CurrentUser(JsonWebToken jwt) {
    this.jwt = jwt;
  }

  public UUID id() {
    String subject = jwt.getSubject();
    if (subject == null) {
      throw new NotAuthorizedException("Not signed in");
    }
    try {
      return UUID.fromString(subject);
    } catch (IllegalArgumentException ex) {
      throw new NotAuthorizedException("Invalid session");
    }
  }
}
