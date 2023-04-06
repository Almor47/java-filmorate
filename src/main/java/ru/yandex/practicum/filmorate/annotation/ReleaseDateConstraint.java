package ru.yandex.practicum.filmorate.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReleaseDateConstraintValidator.class)

public @interface ReleaseDateConstraint {
    String message() default "Дата не может быть до 1895.1.28";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
