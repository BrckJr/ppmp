package io.github.brckjr.ppmp.domain.service.auth;

import io.github.brckjr.ppmp.domain.model.shared.User;
import io.github.brckjr.ppmp.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthServiceTest {

  private static final String PASSWORD = "correct horse battery";

  private final Map<UUID, User> users = new LinkedHashMap<>();
  private AuthService service;

  @BeforeEach
  void setUp() {
    service = new AuthService(new InMemoryUserRepository(), new FakeHasher(), new LoginAttemptLimiter());
  }

  @Test
  void registersUserWithHashedPasswordAndNormalizedNames() {
    User user = service.register("  Alice ", "Alice@Example.com", PASSWORD);

    assertThat(user.getUsername()).isEqualTo("alice");
    assertThat(user.getEmail()).isEqualTo("alice@example.com");
    assertThat(user.getPasswordHash()).isPresent().get().isNotEqualTo(PASSWORD);
    assertThat(user.isActive()).isTrue();
  }

  @Test
  void rejectsInvalidAndDuplicateRegistrations() {
    service.register("alice", "alice@example.com", PASSWORD);

    assertThatThrownBy(() -> service.register("al", "x@example.com", PASSWORD)).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> service.register("bob", "not-an-email", PASSWORD)).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> service.register("bob", "bob@example.com", "short")).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> service.register("bob", "bob@example.com", "x".repeat(73))).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> service.register("ALICE", "other@example.com", PASSWORD)).isInstanceOf(UserAlreadyExistsException.class);
    assertThatThrownBy(() -> service.register("bob", "alice@example.com", PASSWORD)).isInstanceOf(UserAlreadyExistsException.class);
  }

  @Test
  void authenticatesByUsernameOrEmail() {
    User alice = service.register("alice", "alice@example.com", PASSWORD);

    assertThat(service.authenticate("Alice", PASSWORD)).isEqualTo(alice);
    assertThat(service.authenticate("alice@example.com", PASSWORD)).isEqualTo(alice);
  }

  @Test
  void rejectsWrongPasswordsAndUnknownUsersWithTheSameError() {
    service.register("alice", "alice@example.com", PASSWORD);

    assertThatThrownBy(() -> service.authenticate("alice", "wrong password")).isInstanceOf(AuthenticationFailedException.class);
    assertThatThrownBy(() -> service.authenticate("nobody", PASSWORD)).isInstanceOf(AuthenticationFailedException.class);
    assertThatThrownBy(() -> service.authenticate("alice", null)).isInstanceOf(AuthenticationFailedException.class);
  }

  @Test
  void rejectsUsersWithoutPassword() {
    users.put(UUID.randomUUID(), User.create("default@ppmp.local", "default", null, null, "ACTIVE"));

    assertThatThrownBy(() -> service.authenticate("default", "anything at all")).isInstanceOf(AuthenticationFailedException.class);
  }

  @Test
  void blocksLoginAfterTooManyFailures() {
    service.register("alice", "alice@example.com", PASSWORD);
    for (int i = 0; i < LoginAttemptLimiter.MAX_FAILURES; i++) {
      assertThatThrownBy(() -> service.authenticate("alice", "wrong password")).isInstanceOf(AuthenticationFailedException.class);
    }

    assertThatThrownBy(() -> service.authenticate("alice", PASSWORD)).isInstanceOf(TooManyLoginAttemptsException.class);
  }

  private static final class FakeHasher implements PasswordHasher {
    @Override
    public String hash(String rawPassword) {
      return "hashed:" + rawPassword;
    }

    @Override
    public boolean matches(String rawPassword, String hash) {
      return hash.equals("hashed:" + rawPassword);
    }
  }

  private final class InMemoryUserRepository implements UserRepository {
    @Override public Optional<User> findById(UUID id) { return Optional.ofNullable(users.get(id)); }
    @Override public List<User> findAll() { return new ArrayList<>(users.values()); }
    @Override public User persist(User dto) { users.put(dto.getId(), dto); return dto; }
    @Override public User update(UUID uuid, User dto) { users.put(uuid, dto); return dto; }
    @Override public void deleteById(UUID id) { users.remove(id); }
    @Override public long count() { return users.size(); }
    @Override public Optional<User> findByUsername(String username) {
      return users.values().stream().filter(u -> u.getUsername().equals(username)).findFirst();
    }
    @Override public Optional<User> findByEmail(String email) {
      return users.values().stream().filter(u -> u.getEmail().equals(email)).findFirst();
    }
  }
}
