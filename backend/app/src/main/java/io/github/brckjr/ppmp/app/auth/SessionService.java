package io.github.brckjr.ppmp.app.auth;

import io.github.brckjr.ppmp.domain.model.shared.User;
import io.smallrye.jwt.algorithm.SignatureAlgorithm;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.NewCookie;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Duration;
import java.util.Base64;
import java.util.Set;

/**
 * Issues the session token and wraps it in an HttpOnly cookie, so scripts in the browser can never read it.
 * Swapping in an external identity provider later only changes how a token is obtained, not how it is checked.
 */
@ApplicationScoped
public class SessionService {

  private static final int MIN_KEY_BYTES = 32;

  private final String issuer;
  private final String cookieName;
  private final boolean cookieSecure;
  private final Duration sessionDuration;
  private final SecretKey signingKey;

  public SessionService(
    @ConfigProperty(name = "mp.jwt.verify.issuer") String issuer,
    @ConfigProperty(name = "mp.jwt.token.cookie") String cookieName,
    @ConfigProperty(name = "ppmp.auth.cookie-secure") boolean cookieSecure,
    @ConfigProperty(name = "ppmp.auth.session-duration") Duration sessionDuration,
    @ConfigProperty(name = "ppmp.auth.jwt-secret") String jwtSecret
  ) {
    this.issuer = issuer;
    this.cookieName = cookieName;
    this.cookieSecure = cookieSecure;
    this.sessionDuration = sessionDuration;
    byte[] key = Base64.getUrlDecoder().decode(jwtSecret.trim());
    if (key.length < MIN_KEY_BYTES) {
      throw new IllegalStateException("PPMP_JWT_SECRET must be a base64url-encoded key of at least " + MIN_KEY_BYTES + " bytes");
    }
    this.signingKey = new SecretKeySpec(key, "HmacSHA256");
  }

  public NewCookie startSession(User user) {
    String token = Jwt.issuer(issuer)
      .subject(user.getId().toString())
      .upn(user.getUsername())
      .groups(Set.of("user"))
      .expiresIn(sessionDuration)
      .jws().algorithm(SignatureAlgorithm.HS256)
      .sign(signingKey);
    return cookie(token, (int) sessionDuration.toSeconds());
  }

  public NewCookie endSession() {
    return cookie("", 0);
  }

  private NewCookie cookie(String value, int maxAgeSeconds) {
    return new NewCookie.Builder(cookieName)
      .value(value)
      .path("/api")
      .httpOnly(true)
      .secure(cookieSecure)
      .sameSite(NewCookie.SameSite.STRICT)
      .maxAge(maxAgeSeconds)
      .build();
  }
}
