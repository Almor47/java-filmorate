package ru.yandex.practicum.filmorate.conroller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NoUpdateException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();
    static int count = 0;

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) {
        checkValidateUser(user);
        count++;
        user.setId(count);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) {
        checkValidateUser(user);
            if (users.containsKey(user.getId())) {
                users.put(user.getId(), user);
            } else {
                throw new NoUpdateException("Ошибка Update");
            }
        return user;
    }

    @GetMapping("/users")
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    public boolean checkValidateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !(user.getEmail().contains("@"))) {
            throw new ValidationException("Ошибка валидации электронной почты");
        }

        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Ошибка валидации логина");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        return true;
    }

}
