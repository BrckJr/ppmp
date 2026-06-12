package io.github.brckjr.ppmp.domain.model;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

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

    public UUID id() { return id; }
    public OffsetDateTime createdAt() { return createdAt; }
    public OffsetDateTime updatedAt() { return updatedAt; }

    protected void touch(OffsetDateTime now) {
        this.updatedAt = Objects.requireNonNull(now);
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