package home.prozetx.lernenwor.domain.user.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NotBlank
@Size(min = 6, max = 20)
@Pattern(regexp = "^[A-Za-z0-9 !@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~`]+$\n")
@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Password {
}
