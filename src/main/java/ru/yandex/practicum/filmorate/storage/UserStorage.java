package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addUser(User user);

    User updateUser(User user);

    List<User> findAllUsers();

    User findUser(long id);

    void addFriends(long id, long friendId);

    List<User> getListFriends(long id);

    void deleteFriends(long id, long friendId);

    List<User> getCommonListFriends(long id, long otherId);

    void deleteUserById(long id);

}
