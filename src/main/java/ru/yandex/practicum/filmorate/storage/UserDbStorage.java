package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public User addUser(User user) {
        String sql = "insert into USERS(EMAIL,LOGIN,NAME,BIRTHDAY) values (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"USER_ID"});
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setString(1, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql1 = "select USER_ID from USERS where USER_ID = ?";
        long id = jdbcTemplate.queryForObject(sql1, new Object[]{user.getId()}, (rs, rowNum) ->
                rs.getLong("USER_ID"));
        String sql2 = "update USERS set LOGIN = ?, NAME = ?, EMAIL = ?, BIRTHDAY = ? where USER_ID = ?";
        jdbcTemplate.update(sql2, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), id);
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        String sql = "select * from USERS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    private User makeUser(ResultSet rs) throws SQLException {
        long id = rs.getLong("USER_ID");
        String email = rs.getString("EMAIL");
        String login = rs.getString("LOGIN");
        String name = rs.getString("NAME");
        LocalDate birthday = rs.getDate("BIRTHDAY").toLocalDate();
        return User.builder().id(id).login(login).name(name).email(email).birthday(birthday).build();
    }

    @Override
    public User findUser(long id) {
        String sql = "select * from USERS where USER_ID = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNUm) -> makeUser(rs), id);
    }

    @Override
    public void addFriends(long id, long friendId) {
        String sql1 = "select USER_ID from USERS where USER_ID = ?";
        long dbId = jdbcTemplate.queryForObject(sql1, new Object[]{id},
                (rs, rowNum) -> rs.getLong("USER_ID"));
        long dbfriendId = jdbcTemplate.queryForObject(sql1, new Object[]{friendId},
                (rs, rowNum) -> rs.getLong("USER_ID"));
        String sql2 = "insert into FRIENDSHIP(USER_ID,FRIEND_ID) values (?,?)";
        jdbcTemplate.update(sql2, dbId, dbfriendId);
    }

    @Override
    public List<User> getListFriends(long id) {
        String sql1 = "select USER_ID from USERS where USER_ID = ?";
        long dbId = jdbcTemplate.queryForObject(sql1, new Object[]{id},
                (rs, rowNum) -> rs.getLong("USER_ID"));
        String sql2 = "select * from USERS where USER_ID in (select f.FRIEND_ID from USERS u join FRIENDSHIP f" +
                " on u.USER_ID = f.USER_ID where f.USER_ID = ?)";
        return jdbcTemplate.query(sql2, (rs, rowNum) -> makeUser(rs), dbId);
    }

    @Override
    public void deleteFriends(long id, long friendId) {
        String sql1 = "select USER_ID from USERS where USER_ID = ?";
        long dbId = jdbcTemplate.queryForObject(sql1, new Object[]{id},
                (rs, rowNum) -> rs.getLong("USER_ID"));
        long dbfriendId = jdbcTemplate.queryForObject(sql1, new Object[]{friendId},
                (rs, rowNum) -> rs.getLong("USER_ID"));
        String sql2 = "delete from FRIENDSHIP where USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql2, dbId, dbfriendId);
    }

    @Override
    public List<User> getCommonListFriends(long id, long otherId) {
        String sql1 = "select USER_ID from USERS where USER_ID = ?";
        long dbId = jdbcTemplate.queryForObject(sql1, new Object[]{id},
                (rs, rowNum) -> rs.getLong("USER_ID"));
        long dbOtherId = jdbcTemplate.queryForObject(sql1, new Object[]{otherId},
                (rs, rowNum) -> rs.getLong("USER_ID"));
        String sql2 = "select * from USERS where USER_ID in (select distinct FRIEND_ID from FRIENDSHIP " +
                "where FRIEND_ID in (select FRIEND_ID from FRIENDSHIP where USER_ID = ?) and " +
                "FRIEND_ID in (select FRIEND_ID from FRIENDSHIP where USER_ID = ?))";
        return jdbcTemplate.query(sql2, (rs, rowNum) -> makeUser(rs), dbId, dbOtherId);
    }
}
