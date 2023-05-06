package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NoUpdateException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> findAllFilms() {
        String sql = "select * from FILMS f JOIN FILMS_RATING fr ON f.MPA_ID = fr.MPA_ID";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }


    private Film makeFilm(ResultSet rs) throws SQLException {
        long id = rs.getLong("FILM_ID");
        String name = rs.getString("FILM_NAME");
        String description = rs.getString("FILM_DESCRIPTION");
        LocalDate releaseDate = rs.getDate("FILM_RELEASEDATE").toLocalDate();
        int duration = rs.getInt("FILM_DURATION");
        Film film = Film.builder().id(id).name(name).description(description).releaseDate(releaseDate)
                        .duration(duration).mpa(Mpa.builder().id(rs.getInt("MPA_ID"))
                        .name(rs.getString("RATING_NAME")).build()).build();

        String sql1 = "select USER_ID from likes where FILM_ID = ?";
        List<Long> userLikes = jdbcTemplate.query(sql1, (rss, rowNum) -> makeUserId(rss), id);
        String sql2 = "select * from FILM_GENRE fg JOIN GENRE g ON g.GENRE_ID = fg.GENRE_ID where FILM_ID = ?";
        List<Genre> filmGenres = jdbcTemplate.query(sql2, (rss, rowNum) -> makeGenre(rss), id);

        for (Genre genre : filmGenres) {
            film.getGenres().add(genre);
        }

        if (userLikes.contains(null)) {
            return film;
        }

        for (Long oneLike : userLikes) {
            film.getLikes().add(oneLike);
        }
        return film;
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return Genre.builder().id(rs.getInt("GENRE_ID"))
                .name(rs.getString("GENRE_NAME")).build();
    }

    private Long makeUserId(ResultSet rss) throws SQLException {
        long userId;
        try {
            userId = rss.getLong("USER_ID");
        } catch (Exception e) {
            return null;
        }
        return userId;
    }

    private Long makeFilmId(ResultSet rss) throws SQLException {
        long userId;
        try {
            userId = rss.getLong("FILM_ID");
        } catch (Exception e) {
            return null;
        }
        return userId;
    }

    @Override
    public List<Film> getPopularFilms(long count) {
        String sql1 = "select FILM_ID from LIKES group by FILM_ID order by COUNT(FILM_ID) DESC limit ?";
        List<Long> filmID = jdbcTemplate.query(sql1, ((rss, rowNum) -> makeFilmId(rss)), count);
        System.out.println(filmID);
        if (filmID.size() == 0) {
            return findAllFilms();
        }
        List<Film> films = new ArrayList<>();
        String sql2 = "select * from FILMS f JOIN FILMS_RATING fr ON f.MPA_ID = fr.MPA_ID where f.FILM_ID = ?";
        for (Long one : filmID) {
            films.add(jdbcTemplate.queryForObject(sql2, new Object[]{one}, (rs, rowNum) -> makeFilm(rs)));
        }
        return films;
    }

    @Override
    public Film addFilm(Film film) {
        String sql1 = "insert into FILMS(FILM_NAME,FILM_DESCRIPTION,FILM_RELEASEDATE,FILM_DURATION,MPA_ID) " +
                "VALUES (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql1, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        Set<Genre> genres = film.getGenres();
        String sql = "insert into FILM_GENRE(FILM_ID,GENRE_ID) values (?,?)";
        for (Genre genre : genres) {
            jdbcTemplate.update(sql, film.getId(), genre.getId());
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        try {
            String sqlOut = "select FILM_ID from FILMS where FILM_ID = ?";
            long id = jdbcTemplate.queryForObject(sqlOut, new Object[]{film.getId()}, (rs, rowNum) ->
                    rs.getLong("FILM_ID"));
            String sqlIn = "update FILMS set FILM_NAME = ?, FILM_DESCRIPTION = ?, FILM_RELEASEDATE = ?, " +
                    "FILM_DURATION = ?, MPA_ID = ? where FILM_ID = ?";
            jdbcTemplate.update(sqlIn, film.getName(), film.getDescription(), film.getReleaseDate(),
                    film.getDuration(), film.getMpa().getId(), id);
            String sqlDelete = "delete from FILM_GENRE where FILM_ID = ?";
            jdbcTemplate.update(sqlDelete, film.getId());
            String sql = "insert into FILM_GENRE(FILM_ID,GENRE_ID) values (?,?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sql, id, genre.getId());
            }
            return film;
        } catch (Exception e) {
            throw new NoUpdateException("Невозможно обновить данные фильма с id = " + film.getId());
        }
    }

    @Override
    public Film findFilm(long id) {
        try {
            String sql = "select * from FILMS f JOIN FILMS_RATING fr ON f.MPA_ID = fr.MPA_ID where FILM_ID = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, intNum) -> makeFilm(rs));
        } catch (Exception e) {
            throw new NotFoundException("Фильм с id = " + id + "не найден.");
        }
    }

    @Override
    public void putLike(long id, long userId) {
        String sql1 = "select USER_ID from USERS where USER_ID = ?";
        long dbUserId = jdbcTemplate.queryForObject(sql1, new Object[]{userId},
                (rs, rowNum) -> rs.getLong("USER_ID"));
        String sql = "insert into LIKES(USER_ID,FILM_ID) values(?,?)";
        jdbcTemplate.update(sql, dbUserId, id);
    }

    @Override
    public void deleteLike(long id, long userId) {
        String sql1 = "select USER_ID from USERS where USER_ID = ?";
        long dbUserId = jdbcTemplate.queryForObject(sql1, new Object[]{userId},
                (rs, rowNum) -> rs.getLong("USER_ID"));
        String sql = "delete from LIKES where USER_ID = ? and FILM_ID = ?";
        jdbcTemplate.update(sql, dbUserId, id);
    }


}
