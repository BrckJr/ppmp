package io.github.brckjr.ppmp.domain.service.auth;

import io.github.brckjr.ppmp.domain.model.shared.User;
import io.github.brckjr.ppmp.domain.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@ApplicationScoped
public class AuthService {

  static final int MIN_PASSWORD_LENGTH = 10;
  // bcrypt only considers the first 72 bytes of a password
  static final int MAX_PASSWORD_BYTES = 72;
  private static final Pattern USERNAME = Pattern.compile("^[a-z0-9._-]{3,32}$");
  private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

  private final UserRepository userRepository;
  private final PasswordHasher passwordHasher;
  private final LoginAttemptLimiter limiter;
  // Verified against when the user is unknown, so response time does not reveal whether an account exists.
  private final String dummyHash;

  @Inject
  public AuthService(UserRepository userRepository, PasswordHasher passwordHasher, LoginAttemptLimiter limiter) {
    this.userRepository = userRepository;
    this.passwordHasher = passwordHasher;
    this.limiter = limiter;
    this.dummyHash = passwordHasher.hash(UUID.randomUUID().toString());
  }

  public User register(String username, String email, String rawPassword) {
    String normalizedUsername = normalize(username);
    String normalizedEmail = normalize(email);
    if (!USERNAME.matcher(normalizedUsername).matches()) {
      throw new IllegalArgumentException("Username must be 3-32 characters: letters, digits, '.', '_' or '-'");
    }
    if (normalizedEmail.length() > 320 || !EMAIL.matcher(normalizedEmail).matches()) {
      throw new IllegalArgumentException("Email address is invalid");
    }
    validatePassword(rawPassword);

    if (userRepository.findByUsername(normalizedUsername).isPresent()) {
      throw new UserAlreadyExistsException("Username is already taken");
    }
    if (userRepository.findByEmail(normalizedEmail).isPresent()) {
      throw new UserAlreadyExistsException("Email is already registered");
    }
    return userRepository.persist(User.register(normalizedEmail, normalizedUsername, passwordHasher.hash(rawPassword)));
  }

  /** Accepts the username or the email as identifier. */
  public User authenticate(String identifier, String rawPassword) {
    String key = normalize(identifier);
    if (limiter.isBlocked(key)) {
      throw new TooManyLoginAttemptsException();
    }

    Optional<User> user = userRepository.findByUsername(key).or(() -> userRepository.findByEmail(key));
    String hash = user.flatMap(User::getPasswordHash).orElse(dummyHash);
    boolean passwordMatches = rawPassword != null && passwordHasher.matches(rawPassword, hash);

    if (!passwordMatches || user.isEmpty() || user.get().getPasswordHash().isEmpty() || !user.get().isActive()) {
      limiter.recordFailure(key);
      throw new AuthenticationFailedException();
    }
    limiter.reset(key);
    return user.get();
  }

  /** Looks up a user for an already authenticated session; inactive users are treated as unknown. */
  public Optional<User> findActiveUser(UUID userId) {
    Objects.requireNonNull(userId, "User id cannot be null");
    return userRepository.findById(userId).filter(User::isActive);
  }

  private static void validatePassword(String rawPassword) {
    if (rawPassword == null || rawPassword.length() < MIN_PASSWORD_LENGTH) {
      throw new IllegalArgumentException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long");
    }
    if (rawPassword.getBytes(StandardCharsets.UTF_8).length > MAX_PASSWORD_BYTES) {
      throw new IllegalArgumentException("Password is too long (max. " + MAX_PASSWORD_BYTES + " bytes)");
    }
  }

  private static String normalize(String value) {
    return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
  }
}
