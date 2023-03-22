package ru.yandex.practicum.filmorate.conroller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NoUpdateException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class FilmController {

    private final HashMap<Integer, Film> films = new HashMap<>();
    static int count = 0;

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) {

        if (checkValidateFilm(film)) {
            count++;
            film.setId(count);
            films.put(film.getId(), film);
        } else {
            throw new ValidationException("Ошибка валидации");
        }
        return film;

    }


    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {

        if (checkValidateFilm(film)) {
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
            } else {
                throw new NoUpdateException("Ошибка Update");
            }
        } else {
            throw new ValidationException("Ошибка валидации");
        }
        return film;

    }


    @GetMapping("/films")
    public List<Film> findAllFilms() {
        List<Film> filmList = new ArrayList<>();
        for (Film film:films.values()) {
            filmList.add(film);
        }
        return filmList;
    }

    public boolean checkValidateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            return false;
        }

        if (film.getDescription().length() > 200) {
            return false;
        }
        LocalDate minimalDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(minimalDate)) {
            return false;
        }

        if (film.getDuration() <= 0) {
            return false;
        }
        return true;
    }


    /*{
        "id":1,
        "name": "One",
        "description": "Oneeeeeeeeeeeeeeeeee",
        "releaseDate": "2000-10-10",
        "duration": 120
    }*/
}