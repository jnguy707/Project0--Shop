package org.example.model;

import java.util.List;
import java.util.Objects;

public class Item {
    private int id;
    private String name;
    private boolean isAvailable;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString(){
        return this.id + "  |  " + this.name + "  |  " + this.isAvailable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id && isAvailable == item.isAvailable && name.equals(item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isAvailable);
    }
}