package io.github.brckjr.ppmp.domain.model.shared;

public record Price(Money money) {
    public Money valueFor(Quantity qty) { return money.times(qty.value()); }
}
