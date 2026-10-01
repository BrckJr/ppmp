package io.github.brckjr.ppmp.domain.model;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

/***
 * All domain models that also interact with the persistence layer need to implement the
 * BaseModel in order to ensure proper handling of those object. Those objects may also be
 * returned to the app / api layer.
 * Other helper objects that simply live in the domain and may be returned to the app layer
 * shall not implement the BaseModel
 ***/
public abstract class BaseModel {
  private final UUID id;
  private final OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;

  protected BaseModel() {
    this.id = UUID.randomUUID();
    this.createdAt = OffsetDateTime.now();
    this.updatedAt = this.createdAt;
  }

  protected BaseModel(UUID id, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
    this.id = Objects.requireNonNull(id, "ID cannot be null");
    this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt cannot be null");
    this.updatedAt = Objects.requireNonNull(updatedAt, "UpdatedAt cannot be null");
  }

  public UUID getId() {
    return id;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  @Override
  public final boolean equals(Object o) {
    return o instanceof BaseModel b && id.equals(b.id);
  }

  @Override
  public final int hashCode() {
    return id.hashCode();
  }
}
