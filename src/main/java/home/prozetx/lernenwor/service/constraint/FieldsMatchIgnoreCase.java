package home.prozetx.lernenwor.service.constraint;

import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FieldsMatchIgnoreCaseValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FieldsMatchIgnoreCase {
    String firstField();
    String secondField();

    String message() default "Fields are not equal ignoring case";
    Class[] groups() default {};
    Class[] payload() default {};
}
