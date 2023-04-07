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
        setNameIfUnspecified(user);
        count++;
        user.setId(count);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        setNameIfUnspecified(user);
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

    public void setNameIfUnspecified(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

}
