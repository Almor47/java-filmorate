package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public List<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public User findUser(long id) {
        return userStorage.findUser(id);
    }

    public void addFriends(long id, long friendId) {
        userStorage.addFriends(id, friendId);
    }

    public List<User> getListFriends(long id) {
        return userStorage.getListFriends(id);
    }

    public void deleteFriends(long id, long friendId) {
        userStorage.deleteFriends(id, friendId);
    }

    public List<User> getCommonListFriends(long id, long otherId) {
        return userStorage.getCommonListFriends(id, otherId);
    }

    public void deleteUserById(long id) {
        userStorage.deleteUserById(id);
    }

}
