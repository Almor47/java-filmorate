package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.conroller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmTest {

    FilmController filmController = new FilmController();

    @Test
    void emptyNameShouldBeFalse() {
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("Yoda")
                .releaseDate(LocalDate.of(1980,10,10))
                .duration(150)
                .build();
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> filmController.checkValidateFilm(film)
        );
        assertEquals("Неверное название фильма",e.getMessage());

    }

    @Test
    void descriptionLengthMax200ShouldBeFalse() {
        Film film = Film.builder()
                .id(1)
                .name("StarWars")
                .description("YodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYoda " +
                        "YodaYodaYodaYodaYodaYodaYodaYodaYoda YodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYodaa" +
                        "YodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYoda YodaYodaYodaYodaYodaYodaYodaYYoda" +
                        "YodaYodaYYodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYoda" +
                        "odaYodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYodaYoda")
                .releaseDate(LocalDate.of(1980,10,10))
                .duration(150)
                .build();
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> filmController.checkValidateFilm(film)
        );
        assertEquals("Превышена максимальная длина описания фильма",e.getMessage());

    }

    @Test
    void releaseDateBefore1895ShouldBeFalse() {
        Film film = Film.builder()
                .id(1)
                .name("StarWars")
                .description("Yoda")
                .releaseDate(LocalDate.of(1800,10,10))
                .duration(150)
                .build();
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> filmController.checkValidateFilm(film)
        );
        assertEquals("Дата релиза раньше 1895",e.getMessage());
    }

    @Test
    void durationFilmNegativeShouldBeFalse() {
        Film film = Film.builder()
                .id(1)
                .name("StarWars")
                .description("Yoda")
                .releaseDate(LocalDate.of(1980,10,10))
                .duration(-100)
                .build();
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> filmController.checkValidateFilm(film)
        );
        assertEquals("Отрицательная продолжительность фильма",e.getMessage());
    }

    @Test
    void allRightShouldBeTrue() {
        Film film = Film.builder()
                .id(1)
                .name("StarWars")
                .description("Yoda")
                .releaseDate(LocalDate.of(1980,10,10))
                .duration(150)
                .build();
        assertTrue(filmController.checkValidateFilm(film));
    }
}
