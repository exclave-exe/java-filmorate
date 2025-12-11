package ru.yandex.practicum.filmorate.model;

public enum MpaType {
    G(1), PG(2), PG_13(3), R(4), NC_17(5);

    private final int id;

    MpaType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static MpaType fromId(int id) {
        for (MpaType v : values()) {
            if (v.id == id) return v;
        }
        throw new IllegalArgumentException("Unknown MPA id=" + id);
    }
}

