package home.prozetx.lernenwor.service.constraint;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Constraint(validatedBy = FieldsMatchConsiderCaseValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldsMatchConsiderCase {
    String firstField();

    String secondField();

    String message() default "Fields are not equal";
    Class[] groups() default {};
    Class[] payload() default {};
}
