package home.prozetx.lernenwor.service.constraint;

import jakarta.validation.Constraint;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = FieldsMatchConsiderCaseValidator.class)
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface FieldsMatchConsiderCase {
    String firstField();

    String secondField();

    String message() default "Fields are not equal";
    Class[] groups() default {};
    Class[] payload() default {};
}
