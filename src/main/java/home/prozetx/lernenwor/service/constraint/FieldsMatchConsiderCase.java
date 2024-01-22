package home.prozetx.lernenwor.service.constraint;

import jakarta.validation.Constraint;

@Constraint(validatedBy = FieldsMatchConsiderCaseValidator.class)
public @interface FieldsMatchConsiderCase {
    String firstField();

    String secondField();

    String message() default "Fields are not equal";
    Class[] groups() default {};
    Class[] payload() default {};
}
