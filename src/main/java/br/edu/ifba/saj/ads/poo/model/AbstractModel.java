package br.edu.ifba.saj.ads.poo.model;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class AbstractModel<T> {
    private T id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected AbstractModel() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    } 

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override 
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractModel<?> that = (AbstractModel<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id=" + id + "}";
    }
}
