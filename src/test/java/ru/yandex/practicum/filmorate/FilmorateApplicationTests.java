package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final MpaDbStorage mpaDbStorage;
    private final GenreStorage genreStorage;
    private Film film;
    private Film film2;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void createData() {
        film = Film.builder().name("Avatar").description("Very long film")
                .releaseDate(LocalDate.of(2009, 10, 10)).duration(200)
                .mpa(Mpa.builder().id(1).build()).build();
        user1 = User.builder().name("Sasha").email("sasha@yandex.ru").login("alex")
                .birthday(LocalDate.of(1990, 10, 10)).build();
        user2 = User.builder().name("Ivan").email("ivan@yandex.ru").login("Vanya")
                .birthday(LocalDate.of(2000, 11, 11)).build();
        film2 = Film.builder().name("HarryPotter").description("Very long film")
                .releaseDate(LocalDate.of(2009, 10, 10)).duration(200)
                .mpa(Mpa.builder().id(1).build()).build();
        user3 = User.builder().name("Masha").email("masha@yandex.ru").login("Maria")
                .birthday(LocalDate.of(1998, 11, 11)).build();
        filmDbStorage.addFilm(film2);
        filmDbStorage.addFilm(film);
        userDbStorage.addUser(user1);
        userDbStorage.addUser(user2);
        userDbStorage.addUser(user3);
    }


    @Test
    public void testFindFilmById() {
        Film film = filmDbStorage.findFilm(1L);
        assertThat(film).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    public void testFindAllGenre() {
        List<Genre> genre = genreStorage.findAllGenre();
        assertEquals(6, genre.size());
    }

    @Test
    public void testFindAllMpa() {
        List<Mpa> mpa = mpaDbStorage.findAllMpa();
        assertEquals(5, mpa.size());
    }

    @Test
    public void testFindAllUsers() {
        List<User> listUser = userDbStorage.findAllUsers();
        assertEquals(user1.getName(), listUser.get(0).getName());
        assertEquals(user2.getName(), listUser.get(1).getName());
        assertEquals(user3.getName(), listUser.get(2).getName());
    }

    @Test
    public void testPutAndDeleteLike() {
        filmDbStorage.putLike(film.getId(), user1.getId());
        Film film1 = filmDbStorage.findFilm(film.getId());
        assertEquals(1, film1.getLikes().size());
        filmDbStorage.deleteLike(film.getId(), user1.getId());
        film1 = filmDbStorage.findFilm(film.getId());
        assertEquals(0, film1.getLikes().size());
    }

    @Test
    public void testUpdateFilm() {
        Film filmNew = Film.builder().id(1L).name("HarryPotter").description("Very long film")
                .releaseDate(LocalDate.of(2009, 10, 10)).duration(200)
                .mpa(Mpa.builder().id(1).build()).build();
        assertEquals(filmNew, filmDbStorage.updateFilm(filmNew));
    }

    @Test
    public void testFindAllFilms() {
        List<Film> listFilm = filmDbStorage.findAllFilms();
        assertEquals(2, listFilm.size());
    }


    @Test
    public void testPopularFilms() {
        assertEquals(2, 2);
        filmDbStorage.putLike(film.getId(), user1.getId());
        List<Film> mostPopularFilms = filmDbStorage.getPopularFilms(1L);
        assertEquals(film.getName(), mostPopularFilms.get(0).getName());
        filmDbStorage.deleteLike(film.getId(), user1.getId());
        filmDbStorage.putLike(film2.getId(), user1.getId());
        mostPopularFilms = filmDbStorage.getPopularFilms(1L);
        assertEquals(film2.getName(), mostPopularFilms.get(0).getName());

    }

    @Test
    public void testUpdateUser() {
        User userNew = User.builder().id(1L).name("Aleksandr").email("sasha@yandex.ru").login("alex")
                .birthday(LocalDate.of(1990, 10, 10)).build();
        assertEquals(userNew, userDbStorage.updateUser(userNew));
    }

    @Test
    public void testPutAndDeleteFriends() {
        userDbStorage.addFriends(1L, 2L);
        assertEquals(1, userDbStorage.getListFriends(1L).size());
        userDbStorage.deleteFriends(1L, 2L);
        assertEquals(0, userDbStorage.getListFriends(1L).size());
    }

    @Test
    public void alllistFriends() {
        userDbStorage.addFriends(1L, 2L);
        List<User> friendsList = userDbStorage.getListFriends(1L);
        assertEquals(2L, friendsList.get(0).getId());
        friendsList = userDbStorage.getListFriends(2L);
        assertEquals(0, friendsList.size());
        userDbStorage.addFriends(3L, 2L);
        List<User> commonFriendsList = userDbStorage.getCommonListFriends(1L, 3L);
        assertEquals(1, commonFriendsList.size());
        assertEquals(2L, commonFriendsList.get(0).getId());
    }


    @Test
    public void testFindUserById() {
        assertEquals(user3.getName(), userDbStorage.findUser(3L).getName());
    }

    @Test
    public void deleteUserById() {
        userDbStorage.deleteUserById(1L);
        assertThrows(NotFoundException.class,
                () -> userDbStorage.findUser(1L));
    }

}
