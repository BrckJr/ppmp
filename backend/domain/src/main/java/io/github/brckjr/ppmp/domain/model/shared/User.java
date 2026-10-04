package io.github.brckjr.ppmp.domain.model.shared;

import io.github.brckjr.ppmp.domain.model.BaseModel;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class User extends BaseModel {

  public static final String STATUS_ACTIVE = "ACTIVE";

  private final String email;
  private final String username;
  private final String firstName;
  private final String lastName;
  private final String userStatus;
  private final String passwordHash;

  private User(String email, String username, String firstName, String lastName, String userStatus, String passwordHash) {
    super();
    this.email = email;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.userStatus = userStatus;
    this.passwordHash = passwordHash;
  }

  public static User create(String email, String username, String firstName, String lastName, String userStatus) {
    Objects.requireNonNull(email, "Email cannot be null");
    Objects.requireNonNull(username, "Username cannot be null");
    return new User(email, username, firstName, lastName, userStatus, null);
  }

  /** Creates an active user that can log in with a password. */
  public static User register(String email, String username, String passwordHash) {
    Objects.requireNonNull(email, "Email cannot be null");
    Objects.requireNonNull(username, "Username cannot be null");
    Objects.requireNonNull(passwordHash, "Password hash cannot be null");
    return new User(email, username, null, null, STATUS_ACTIVE, passwordHash);
  }

  public static User reconstitute(
      UUID id,
      OffsetDateTime createdAt,
      OffsetDateTime updatedAt,
      String email,
      String username,
      String firstName,
      String lastName,
      String userStatus,
      String passwordHash
  ) {
    return new User(id, createdAt, updatedAt, email, username, firstName, lastName, userStatus, passwordHash);
  }

  private User(
      UUID id,
      OffsetDateTime createdAt,
      OffsetDateTime updatedAt,
      String email,
      String username,
      String firstName,
      String lastName,
      String userStatus,
      String passwordHash
  ) {
    super(id, createdAt, updatedAt);
    this.email = email;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.userStatus = userStatus;
    this.passwordHash = passwordHash;
  }


  // --- Domain Behaviors ---

  public boolean isActive() {
    return STATUS_ACTIVE.equals(userStatus);
  }

  // --- Getters ---
  public String email() {
    return email;
  }

  public String getEmail() {
    return email;
  }

  public String username() {
    return username;
  }

  public String getUsername() {
    return username;
  }

  public Optional<String> getFirstName() {
    return Optional.ofNullable(firstName);
  }

  public Optional<String> getLastName() {
    return Optional.ofNullable(lastName);
  }

  public Optional<String> getPasswordHash() {
    return Optional.ofNullable(passwordHash);
  }

  public Optional<String> getUserStatus() {
    return Optional.ofNullable(userStatus);
  }

}
