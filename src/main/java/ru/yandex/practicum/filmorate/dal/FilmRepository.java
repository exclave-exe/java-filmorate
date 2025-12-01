package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FilmRepository extends BaseRepository<Film> {
    private static final String INSERT_FILM_GENRE = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
    private static final String DELETE_FILM_GENRES = "DELETE FROM film_genres WHERE film_id = ?";
    private static final String DELETE_LIKE = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";

    private static final String INSERT_FILM = """
            INSERT INTO films (name, description, release_date, duration, mpa_id)
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String INSERT_LIKE = """
            INSERT INTO film_likes (film_id, user_id)
            VALUES (?, ?)
            """;

    private static final String SELECT_FILM_GENRES_BY_FILMS_ID = """
            SELECT fg.film_id, g.id, g.name
            FROM film_genres fg
            JOIN genres g ON g.id = fg.genre_id
            WHERE fg.film_id IN (%s)
            ORDER BY g.id
            """;

    private static final String UPDATE_FILM = """
            UPDATE films
            SET name         = ?,
                description  = ?,
                release_date = ?,
                duration     = ?,
                mpa_id       = ?
            WHERE id = ?
            """;

    private static final String SELECT_ALL_FILMS = """
            SELECT f.id,
                   f.name,
                   f.description,
                   f.release_date,
                   f.duration,
                   f.mpa_id,
                   m.name AS mpa_name
            FROM films f
            JOIN mpa m ON m.id = f.mpa_id
            ORDER BY f.id
            """;

    private static final String SELECT_FILM_BY_ID = """
            SELECT f.id,
                   f.name,
                   f.description,
                   f.release_date,
                   f.duration,
                   f.mpa_id,
                   m.name AS mpa_name
            FROM films f
            JOIN mpa m ON m.id = f.mpa_id
            WHERE f.id = ?
            """;

    private static final String SELECT_POPULAR_FILMS = """
            SELECT f.id,
                   f.name,
                   f.description,
                   f.release_date,
                   f.duration,
                   f.mpa_id,
                   m.name AS mpa_name
            FROM films f
            JOIN mpa m ON m.id = f.mpa_id
            LEFT JOIN film_likes fl ON f.id = fl.film_id
            GROUP BY f.id, f.name, f.description, f.release_date, f.duration, f.mpa_id, m.name
            ORDER BY COUNT(fl.user_id) DESC, f.id
            LIMIT ?
            """;

    private final RowMapper<Genre> genreRowMapper;

    public FilmRepository(JdbcTemplate jdbcTemplate, RowMapper<Film> filmRowMapper, RowMapper<Genre> genreRowMapper) {
        super(jdbcTemplate, filmRowMapper);
        this.genreRowMapper = genreRowMapper;
    }

    public Collection<Film> getFilms() {
        Collection<Film> films = findMany(SELECT_ALL_FILMS);
        loadGenresForFilms(films);
        return films;
    }

    public Optional<Film> getFilmById(Long id) {
        Optional<Film> filmOpt = findOne(SELECT_FILM_BY_ID, id);
        filmOpt.ifPresent(film -> loadGenresForFilms(List.of(film)));
        return filmOpt;
    }

    public Long createFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_FILM, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);

        Long filmId = keyHolder.getKey().longValue();
        saveFilmGenres(filmId, film.getGenres());
        return filmId;
    }

    public void updateFilm(Film film) {
        jdbcTemplate.update(UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        jdbcTemplate.update(DELETE_FILM_GENRES, film.getId());
        saveFilmGenres(film.getId(), film.getGenres());
    }

    public Collection<Film> getPopularFilms(long count) {
        Collection<Film> films = findMany(SELECT_POPULAR_FILMS, count);
        loadGenresForFilms(films);
        return films;
    }

    public void addLike(long filmId, long userId) {
        jdbcTemplate.update(INSERT_LIKE, filmId, userId);
    }

    public void deleteLike(long filmId, long userId) {
        jdbcTemplate.update(DELETE_LIKE, filmId, userId);
    }

    private void saveFilmGenres(Long filmId, Set<Genre> genres) {
        if (genres == null || genres.isEmpty()) return;

        Set<Integer> genreIds = genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        jdbcTemplate.batchUpdate(
                INSERT_FILM_GENRE,
                genreIds,
                genreIds.size(),
                (ps, genreId) -> {
                    ps.setLong(1, filmId);
                    ps.setInt(2, genreId);
                }
        );
    }

    private void loadGenresForFilms(Collection<Film> films) {
        if (films == null || films.isEmpty()) return;

        List<Long> filmIds = films.stream()
                .map(Film::getId)
                .toList();

        String placeholders = filmIds.stream()
                .map(id -> "?")
                .collect(Collectors.joining(", "));

        String sql = SELECT_FILM_GENRES_BY_FILMS_ID.formatted(placeholders);

        Map<Long, Set<Genre>> genresByFilmId = new LinkedHashMap<>();

        jdbcTemplate.query(sql, filmIds.toArray(), rs -> {
            Long filmId = rs.getLong("film_id");
            Genre genre = genreRowMapper.mapRow(rs, -1);
            genresByFilmId.computeIfAbsent(filmId, k -> new LinkedHashSet<>()).add(genre);
        });

        films.forEach(film -> {
            Set<Genre> genres = genresByFilmId.getOrDefault(film.getId(), new LinkedHashSet<>());
            film.setGenres(genres);
        });
    }
}
