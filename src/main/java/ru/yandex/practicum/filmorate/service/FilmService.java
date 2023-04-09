package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FilmService {

    private final InMemoryFilmStorage inMemoryFilmStorage;

    public Film putLike(long id, long userId) {
        if (inMemoryFilmStorage.films.get(id) == null) {
            throw new NotFoundException("Фильма с таким ID не существует");
        }
        inMemoryFilmStorage.films.get(id).getLikes().add(userId);
        return inMemoryFilmStorage.films.get(id);
    }

    public Film deleteLike(long id, long userId) {
        if (inMemoryFilmStorage.films.get(id).getLikes().contains(userId)) {
            inMemoryFilmStorage.films.get(id).getLikes().remove(userId);
        } else {
            throw new NotFoundException("Пользователь с таким ID лайк не ставил");
        }
        return inMemoryFilmStorage.films.get(id);
    }

    public List<Film> getPopularFilms(long count) {
        return new ArrayList<>(inMemoryFilmStorage.films.values()).stream()
                .sorted((x, y) -> (-x.getLikes().size()) + y.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film findFilm(long id) {
        if (inMemoryFilmStorage.films.get(id) == null) {
            throw new NotFoundException("Фильма с таким ID не существует");
        }
        return inMemoryFilmStorage.films.get(id);
    }
}
