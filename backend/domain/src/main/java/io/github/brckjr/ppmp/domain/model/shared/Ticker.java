package io.github.brckjr.ppmp.domain.model.shared;

import java.util.Locale;

public record Ticker(String symbol) {
    public Ticker {
        if (symbol == null || symbol.isBlank())
            throw new IllegalArgumentException("Ticker must not be blank");
        symbol = symbol.toUpperCase(Locale.ROOT);
    }
}
