package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashSet;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();

        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        film.setReleaseDate(releaseDate);
        film.setDuration(rs.getInt("duration"));

        Mpa mpa = new Mpa();
        mpa.setId(rs.getInt("mpa_id"));
        try {
            String mpaName = rs.getString("mpa_name");
            if (mpaName != null) {
                mpa.setName(mpaName);
            }
        } catch (SQLException ignored) {
        }
        film.setMpa(mpa);

        film.setGenres(new LinkedHashSet<Genre>());

        return film;
    }
}
