package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> findAllFilms();

    List<Film> getPopularFilms(long count);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film findFilm(long id);

    void putLike(long id, long userId);

    void deleteLike(long id, long userId);


}
