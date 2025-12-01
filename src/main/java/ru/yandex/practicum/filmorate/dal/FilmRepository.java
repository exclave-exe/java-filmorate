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
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class FilmRepository extends BaseRepository<Film> {
    private static final String SELECT_ALL_FILMS =
            "SELECT * FROM films ORDER BY id";

    private static final String SELECT_FILM_BY_ID =
            "SELECT * FROM films WHERE id = ?";

    private static final String INSERT_FILM = """
        INSERT INTO films (name, description, release_date, duration, mpa_id)
        VALUES (?, ?, ?, ?, ?)
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

    private static final String INSERT_LIKE = """
        INSERT INTO film_likes (film_id, user_id)
        VALUES (?, ?)
        """;

    private static final String DELETE_LIKE =
            "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";

    private static final String SELECT_POPULAR_FILMS = """
        SELECT f.*
        FROM films f
        LEFT JOIN film_likes fl ON f.id = fl.film_id
        GROUP BY f.id
        ORDER BY COUNT(fl.user_id) DESC, f.id
        LIMIT ?
        """;

    private static final String INSERT_FILM_GENRE =
            "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";

    private static final String DELETE_FILM_GENRES =
            "DELETE FROM film_genres WHERE film_id = ?";

    private static final String SELECT_FILM_GENRES = """
        SELECT g.id, g.name
        FROM genres g
        JOIN film_genres fg ON g.id = fg.genre_id
        WHERE fg.film_id = ?
        ORDER BY g.id
        """;

    private final RowMapper<Genre> genreRowMapper;
    private final MpaRepository mpaRepository;

    public FilmRepository(JdbcTemplate jdbcTemplate,
                          RowMapper<Film> filmRowMapper,
                          RowMapper<Genre> genreRowMapper,
                          MpaRepository mpaRepository) {
        super(jdbcTemplate, filmRowMapper);
        this.genreRowMapper = genreRowMapper;
        this.mpaRepository = mpaRepository;
    }

    public Collection<Film> getFilms() {
        Collection<Film> films = findMany(SELECT_ALL_FILMS);
        films.forEach(f -> {
            loadFilmGenres(f);
            loadFilmMpa(f);
        });
        return films;
    }

    public Optional<Film> getFilmById(Long id) {
        Optional<Film> filmOpt = findOne(SELECT_FILM_BY_ID, id);
        filmOpt.ifPresent(f -> {
            loadFilmGenres(f);
            loadFilmMpa(f);
        });
        return filmOpt;
    }

    public Long createFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    INSERT_FILM,
                    Statement.RETURN_GENERATED_KEYS
            );
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
        films.forEach(f -> {
            loadFilmGenres(f);
            loadFilmMpa(f);
        });
        return films;
    }

    public void addLike(long filmId, long userId) {
        jdbcTemplate.update(INSERT_LIKE, filmId, userId);
    }

    public void deleteLike(long filmId, long userId) {
        jdbcTemplate.update(DELETE_LIKE, filmId, userId);
    }

    private void saveFilmGenres(Long filmId, Set<Genre> genres) {
        if (genres == null || genres.isEmpty()) {
            return;
        }
        Set<Integer> ids = genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        for (Integer genreId : ids) {
            jdbcTemplate.update(INSERT_FILM_GENRE, filmId, genreId);
        }
    }

    private void loadFilmGenres(Film film) {
        Set<Genre> genres = new LinkedHashSet<>(
                jdbcTemplate.query(SELECT_FILM_GENRES, genreRowMapper, film.getId())
        );
        film.setGenres(genres);
    }

    private void loadFilmMpa(Film film) {
        if (film.getMpa() == null || film.getMpa().getId() == null) {
            return;
        }
        mpaRepository.getMpaById(film.getMpa().getId())
                .ifPresent(film::setMpa);
    }
}
