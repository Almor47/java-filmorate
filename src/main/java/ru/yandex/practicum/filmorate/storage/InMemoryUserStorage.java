package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NoUpdateException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {

    static int count = 0;
    public HashMap<Long, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        checkValidateUser(user);
        count++;
        user.setId(count);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        checkValidateUser(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new NoUpdateException("Ошибка Update, такого пользователя нет");
        }
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    public boolean checkValidateUser(User user) {

        /*if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Ошибка валидации логина");
        }*/
        /*if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }*/

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        return true;
    }

}
