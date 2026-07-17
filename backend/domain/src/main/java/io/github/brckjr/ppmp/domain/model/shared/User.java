package io.github.brckjr.ppmp.domain.model.shared;

import io.github.brckjr.ppmp.domain.model.BaseModel;

import java.util.Objects;
import java.util.Optional;

public class User extends BaseModel {

  private final String email;
  private final String username;
  private final String firstName;
  private final String lastName;
  private final String userStatus;

  private User(String email, String username, String firstName, String lastName, String userStatus) {
    super();
    this.email = email;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.userStatus = userStatus;
  }

  public static User create(String email, String username, String firstName, String lastName, String userStatus) {
    Objects.requireNonNull(email, "Email cannot be null");
    Objects.requireNonNull(username, "Username cannot be null");
    return new User(email, username, firstName, lastName, userStatus);
  }

  public static User reconstitute(String email, String username, String firstName, String lastName, String userStatus) {
    return new User(email, username, firstName, lastName, userStatus);
  }


  // --- Domain Behaviors ---


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

  public Optional<String> getUserStatus() {
    return Optional.ofNullable(userStatus);
  }

}
