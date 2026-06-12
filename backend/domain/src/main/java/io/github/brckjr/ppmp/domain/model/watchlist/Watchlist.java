package io.github.brckjr.ppmp.domain.model.watchlist;

import io.github.brckjr.ppmp.domain.model.BaseModel;
import io.github.brckjr.ppmp.domain.model.shared.User;

import java.util.*;

public final class Watchlist extends BaseModel {
    private final User user;
    private final String name;
    private final String description;
    private final List<WatchlistItem> items;

    private Watchlist(User user, String name, String description, List<WatchlistItem> items) {
        super();
        this.user = user;
        this.name = name;
        this.description = description;
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
    }

    public static Watchlist create(User user, String name, String description, List<WatchlistItem> items) {
        Objects.requireNonNull(user, "User cannot be null");
        Objects.requireNonNull(name, "Watchlist name cannot be null");
        return new Watchlist(user, name, description, items);
    }

    public static Watchlist reconstitute(User user, String name, String description, List<WatchlistItem> items) {
        return new Watchlist(user, name, description, items);
    }

    // --- Domain Behaviors ---
    public void addItem(WatchlistItem item) {
        items.add(item);
    }

    // --- Getters ---

    public User user() { return user; }

    public String name() {
        return name;
    }

    public Optional<String> description() {
        return Optional.ofNullable(description);
    }

    public List<WatchlistItem> items() {
        return Collections.unmodifiableList(items);
    }
}

