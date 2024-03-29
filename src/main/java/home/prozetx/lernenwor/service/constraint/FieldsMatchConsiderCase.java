package home.prozetx.lernenwor.service.constraint;

import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FieldsMatchConsiderCaseValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldsMatchConsiderCase {
    String firstField();

    String secondField();

    String message() default "Fields are not equal considering case";
    Class[] groups() default {};
    Class[] payload() default {};
}
