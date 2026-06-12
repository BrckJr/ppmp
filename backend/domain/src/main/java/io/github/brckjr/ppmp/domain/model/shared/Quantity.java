package io.github.brckjr.ppmp.domain.model.shared;

import java.math.BigDecimal;

public record Quantity(BigDecimal value) {
    public Quantity {
        if (value == null || value.signum() < 0)
            throw new IllegalArgumentException("Quantity must be non-negative");
    }
    public Quantity plus(Quantity q)  { return new Quantity(value.add(q.value)); }
    public Quantity minus(Quantity q) { return new Quantity(value.subtract(q.value)); }
    public boolean isZero()           { return value.signum() == 0; }
}
