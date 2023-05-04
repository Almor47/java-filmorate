package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmValidationTest {
    static Validator validator;

    /*@BeforeAll
    public static void initialization() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    public void releaseDateBeforeShouldBeFalse() {
        Film film = Film.builder()
                .id(1)
                .name("StarWars")
                .description("Yoda")
                .releaseDate(LocalDate.of(1800, 10, 10))
                .duration(150)
                .build();
        Set<ConstraintViolation<Film>> validates = validator.validate(film);
        assertTrue(validates.size() > 0);
    }

    @Test
    public void releaseDateBeforeShouldBeTrue() {
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("Yoda")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(150)
                .build();
        Set<ConstraintViolation<Film>> validates = validator.validate(film);
        assertTrue(validates.size() > 0);

    }*/

}
