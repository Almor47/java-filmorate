package ru.yandex.practicum.filmorate.conroller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable long id) {
        return userService.findUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriends(@PathVariable long id, @PathVariable long friendId) {
        userService.addFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getListFriends(@PathVariable long id) {
        return userService.getListFriends(id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable long id, @PathVariable long friendId) {
        userService.deleteFriends(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonListFriends(@PathVariable long id,
                                           @PathVariable long otherId) {
        return userService.getCommonListFriends(id, otherId);
    }

}
