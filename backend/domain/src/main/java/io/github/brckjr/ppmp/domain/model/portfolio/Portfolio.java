package io.github.brckjr.ppmp.domain.model.portfolio;

import io.github.brckjr.ppmp.domain.model.BaseModel;
import io.github.brckjr.ppmp.domain.model.shared.User;

import java.util.Objects;
import java.util.Optional;

public class Portfolio extends BaseModel {

    private final User user;
    private final String name;
    private final String description;
    private final String baseCurrency;


    private Portfolio(
            User user,
            String name,
            String description,
            String baseCurrency
    ) {
        super();
        this.user = user;
        this.name = name;
        this.description = description;
        this.baseCurrency = baseCurrency;
    }

    public static Portfolio create(
            User user,
            String name,
            String description,
            String baseCurrency
    ) {
        Objects.requireNonNull(user, "Portfolio user cannot be null");
        Objects.requireNonNull(name, "Portfolio name cannot be null");
        Objects.requireNonNull(baseCurrency, "Portfolio base currency cannot be null");

        return new Portfolio(user, name, description, baseCurrency);
    }

    public static Portfolio reconstitute(
            User user,
            String name,
            String description,
            String baseCurrency
    ) {
        return new Portfolio(user, name, description, baseCurrency);
    }

    // --- Domain Behaviors ---


    // --- Getters ---

    public User user() {
        return user;
    }

    public String name() {
        return name;
    }

    public Optional<String> description() {
        return Optional.ofNullable(description);
    }

    public String baseCurrency() {
        return baseCurrency;
    }
}