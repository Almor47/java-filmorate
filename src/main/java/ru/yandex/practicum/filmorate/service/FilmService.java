package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;

    public FilmService(FilmStorage filmStorage, GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.genreStorage = genreStorage;
    }

    public List<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public List<Film> getPopularFilms(long count) {
        return filmStorage.getPopularFilms(count);
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film findFilm(long id) {
        return filmStorage.findFilm(id);
    }

    public List<Genre> findAllGenre() {
        return genreStorage.findAllGenre();
    }

    public Genre findFilmGenres(int id) {
        return genreStorage.findFilmGenres(id);
    }

    public void putLike(long id, long userId) {
        filmStorage.putLike(id, userId);
    }

    public void deleteLike(long id, long userId) {
        filmStorage.deleteLike(id, userId);
    }

}
