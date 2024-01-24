package home.prozetx.lernenwor.service.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.PropertyAccessorFactory;

import java.util.Objects;

import static java.lang.String.format;

public class FieldsMatchConsiderCaseValidator implements ConstraintValidator<FieldsMatchConsiderCase, Object> {
    private String firstField;
    private String secondField;
    private String message;

    @Override
    public void initialize(FieldsMatchConsiderCase constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        firstField = constraintAnnotation.firstField();
        secondField = constraintAnnotation.secondField();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        final var beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(o);
        final var firstObj = beanWrapper.getPropertyValue(firstField);
        final var secondObj = beanWrapper.getPropertyValue(secondField);

        boolean isValid = Objects.equals(firstObj, secondObj);
        if (!isValid) {
//            constraintValidatorContext.disableDefaultConstraintViolation();
//            constraintValidatorContext.buildConstraintViolationWithTemplate(format(message, firstObj, secondObj))
//                    .addPropertyNode(firstField)
//                    .addConstraintViolation();
//            constraintValidatorContext.buildConstraintViolationWithTemplate(format(message, firstObj, secondObj))
//                    .addPropertyNode(secondField)
//                    .addConstraintViolation();
        }
        return isValid;
    }
}
