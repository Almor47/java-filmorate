package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> findAllMpa() {
        String sql = "select * from FILMS_RATING";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
    }

    @Override
    public Mpa findMpa(int id) {
        String sql = "select * from FILMS_RATING where MPA_ID = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeMpa(rs), id);

    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        int mpaId = rs.getInt("MPA_ID");
        String mpaName = rs.getString("RATING_NAME");
        return Mpa.builder().id(mpaId).name(mpaName).build();
    }
}
