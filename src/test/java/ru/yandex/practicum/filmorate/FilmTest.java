package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.conroller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertFalse(filmController.checkValidateFilm(film));
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
        assertFalse(filmController.checkValidateFilm(film));
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
        assertFalse(filmController.checkValidateFilm(film));
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
        assertFalse(filmController.checkValidateFilm(film));
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
