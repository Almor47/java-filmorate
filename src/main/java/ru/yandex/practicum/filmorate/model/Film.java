package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.ReleaseDateConstraint;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
@Builder
public class Film {

    private final Set<Genre> genres = new TreeSet<>();
    @NotEmpty
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @ReleaseDateConstraint
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private final Set<Long> likes = new HashSet<>();
    private long id;
    private Mpa mpa;

}
