package ru.yandex.practicum.filmorate.model;

public enum GenreType {
    COMEDY(1, "Комедия"),
    DRAMA(2, "Драма"),
    CARTOON(3, "Мультфильм"),
    THRILLER(4, "Триллер"),
    DOCUMENTARY(5, "Документальный"),
    ACTION(6, "Боевик");

    private final int id;

    GenreType(int id, String title) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static GenreType fromId(int id) {
        for (GenreType g : values()) {
            if (g.id == id) {
                return g;
            }
        }
        throw new IllegalArgumentException("Unknown genre id=" + id);
    }
}
