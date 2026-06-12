package io.github.brckjr.ppmp.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "watchlist", schema = "ppmp")
public class WatchlistEntity extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_uuid", nullable = false)
    private UserEntity user;

    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "watchlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WatchlistItemEntity> items = new ArrayList<>();

    // Helper method to ensure the back-reference (foreign key) is populated
    public void addItem(WatchlistItemEntity item) {
        items.add(item);
        item.setWatchlist(this);
    }

    public List<WatchlistItemEntity> getItems() {
        return items;
    }

    public void setItems(List<WatchlistItemEntity> items) {
        this.items = items;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}