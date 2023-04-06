package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final InMemoryUserStorage inMemoryUserStorage;

    public User addFriends(long id, long friendId) {

        if (inMemoryUserStorage.users.get(id) == null || inMemoryUserStorage.users.get(friendId) == null) {
            throw new NotFoundException("Пользователя с таким ID не существует");
        }
        inMemoryUserStorage.users.get(id).getFriends().add(friendId);
        inMemoryUserStorage.users.get(friendId).getFriends().add(id);
        return inMemoryUserStorage.users.get(id);
    }

    public User deleteFriends(long id, long friendId) {

        if (inMemoryUserStorage.users.get(id).getFriends().contains(friendId)) {
            inMemoryUserStorage.users.get(id).getFriends().remove(friendId);
            inMemoryUserStorage.users.get(friendId).getFriends().remove(id);
        } else {
            throw new NotFoundException("Пользователя с таким ID не существует");
        }
        return inMemoryUserStorage.users.get(id);
    }

    public Set<User> getListFriends(long id) {
        Set<User> listFriends = new HashSet<>();
        for (Long user : inMemoryUserStorage.users.get(id).getFriends()) {
            listFriends.add(inMemoryUserStorage.users.get(user));
        }
        return listFriends;
    }

    public Set<User> getCommonListFriends(long id, long otherId) {
        Set<User> commonFriendList = new HashSet<>();
        if (inMemoryUserStorage.users.get(id) == null || inMemoryUserStorage.users.get(otherId) == null) {
            throw new NotFoundException("Пользователя с таким ID не существует");
        }
        if (inMemoryUserStorage.users.get(id).getFriends().size() == 0 ||
                inMemoryUserStorage.users.get(otherId).getFriends().size() == 0) {
            return commonFriendList;
        }
        for (Long friendId : inMemoryUserStorage.users.get(id).getFriends()) {
            if (inMemoryUserStorage.users.get(otherId).getFriends().contains(friendId)) {
                commonFriendList.add(inMemoryUserStorage.users.get(friendId));
            }
        }
        return commonFriendList;
    }

    public User findUser(long id) {
        if (inMemoryUserStorage.users.get(id) == null) {
            throw new NotFoundException("Пользователя с таким ID не существует");
        }
        return inMemoryUserStorage.users.get(id);
    }

}
