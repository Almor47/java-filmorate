package ru.yandex.practicum.filmorate.conroller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final InMemoryUserStorage inMemoryUserStorage;
    private final UserService userService;

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return inMemoryUserStorage.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return inMemoryUserStorage.updateUser(user);
    }

    @GetMapping
    public List<User> findAllUsers() {
        return inMemoryUserStorage.findAllUsers();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriends(@PathVariable long id, @PathVariable long friendId) {
        return userService.addFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriends(@PathVariable long id, @PathVariable long friendId) {
        return userService.deleteFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Set<User> getListFriends(@PathVariable long id) {
        return userService.getListFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getCommonListFriends(@PathVariable long id,
                                          @PathVariable long otherId) {
        return userService.getCommonListFriends(id, otherId);
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable long id) {
        return userService.findUser(id);
    }


}
