package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {

    private final Set<Long> friends = new HashSet<>();
    private long id;
    @Email
    private String email;
    @NotEmpty
    @Pattern(regexp = "\\S+")
    private String login;
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;

}
