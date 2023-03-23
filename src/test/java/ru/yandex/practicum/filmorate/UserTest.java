package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.conroller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserTest {

    UserController userController = new UserController();

    @Test
    void emptyEmailShouldBeFalse() {
        User user = User.builder()
                .id(1)
                .email(" ")
                .name("Lombok")
                .login("MyLogin")
                .birthday(LocalDate.of(1990,10,10))
                .build();
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> userController.checkValidateUser(user)
        );
        assertEquals("Ошибка валидации электронной почты",e.getMessage());
    }

    @Test
    void emailWithoutAShouldBeFalse() {
        User user = User.builder()
                .id(1)
                .email("Lombok.yandex.ru")
                .name("Lombok")
                .login("MyLogin")
                .birthday(LocalDate.of(1990,10,10))
                .build();
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> userController.checkValidateUser(user)
        );
        assertEquals("Ошибка валидации электронной почты",e.getMessage());
    }

    @Test
    void emptyLoginShouldBeFalse() {
        User user = User.builder()
                .id(1)
                .email("Lombok@yandex.ru")
                .name("Lombok")
                .login("")
                .birthday(LocalDate.of(1990,10,10))
                .build();
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> userController.checkValidateUser(user)
        );
        assertEquals("Ошибка валидации логина",e.getMessage());
    }

    @Test
    void loginWithSpaceShouldBeFalse() {
        User user = User.builder()
                .id(1)
                .email("Lombok@yandex.ru")
                .name("Lombok")
                .login("sasha sasha")
                .birthday(LocalDate.of(1990,10,10))
                .build();
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> userController.checkValidateUser(user)
        );
        assertEquals("Ошибка валидации логина",e.getMessage());
    }

    @Test
    void birthdayInFutureShouldBeFalse() {
        User user = User.builder()
                .id(1)
                .email("Lombok@yandex.ru")
                .name("Lombok")
                .login("sashaSasha")
                .birthday(LocalDate.of(2220,10,10))
                .build();
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> userController.checkValidateUser(user)
        );
        assertEquals("Дата рождения не может быть в будущем",e.getMessage());
    }

    @Test
    void nameEmptyShouldEqualsLogin() {
        User user = User.builder()
                .id(1)
                .email("Lombok@yandex.ru")
                .name("")
                .login("sashaSasha")
                .birthday(LocalDate.of(2000,10,10))
                .build();
        userController.checkValidateUser(user);
        assertEquals(user.getLogin(),user.getName());
    }

    @Test
    void allRightShouldBeTrue() {
        User user = User.builder()
                .id(1)
                .email("Lombok@yandex.ru")
                .name("Sasha")
                .login("sashaSasha")
                .birthday(LocalDate.of(1980,10,10))
                .build();
        assertTrue(userController.checkValidateUser(user));
    }

}
