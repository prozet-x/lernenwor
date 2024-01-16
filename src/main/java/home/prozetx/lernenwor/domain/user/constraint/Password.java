package home.prozetx.lernenwor.domain.user.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NotBlank
@Size(min = 6, max = 20)
@Pattern(regexp = "^[a-zA-Zа-яА-Я0-9][a-zA-Zа-яА-Я0-9-_]*$")
@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Password {
    String message() default "Invalid pass";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
