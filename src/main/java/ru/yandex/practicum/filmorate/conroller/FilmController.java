package ru.yandex.practicum.filmorate.conroller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NoUpdateException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class FilmController {

    private final HashMap<Integer, Film> films = new HashMap<>();
    static int count = 0;

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        checkValidateFilm(film);
        count++;
        film.setId(count);
        films.put(film.getId(), film);
        return film;
    }


    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new NoUpdateException("Ошибка Update");
        }
        return film;
    }


    @GetMapping("/films")
    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    public boolean checkValidateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Неверное название фильма");
        }

        if (film.getDescription().length() > 200) {
            throw new ValidationException("Превышена максимальная длина описания фильма");
        }
        LocalDate minimalDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(minimalDate)) {
            throw new ValidationException("Дата релиза раньше 1895");
        }

        if (film.getDuration() <= 0) {
            throw new ValidationException("Отрицательная продолжительность фильма");
        }
        return true;

    }

}