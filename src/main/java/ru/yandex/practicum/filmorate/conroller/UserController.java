package ru.yandex.practicum.filmorate.conroller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NoUpdateException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();
    static int count = 0;

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {

        if (checkValidateUser(user)) {
            count++;
            user.setId(count);
            users.put(user.getId(), user);
        } else {
            throw new ValidationException("Ошибка валидации");
        }

        return user;

    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {

        if (checkValidateUser(user)) {
            if (users.containsKey(user.getId())) {
                users.put(user.getId(), user);
            } else {
                throw new NoUpdateException("Ошибка Update");
            }
        } else {
            throw new ValidationException("Ошибка валидации");
        }

        return user;

    }

    @GetMapping("/users")
    public List<User> findAllUsers() {
        List<User> userList = new ArrayList<>();
        for (User user:users.values()) {
            userList.add(user);
        }
        return userList;
    }

    public boolean checkValidateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !(user.getEmail().contains("@"))) {
            return false;
        }

        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            return false;
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            return false;
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }


        return true;
    }


    /*id{
        "id":3,
            "email": "fdsfsdfsdf@ndex.ru",
            "login": "Masha",
            "name": "",
            "birthday": "2024-02-08"
    }*/
}
