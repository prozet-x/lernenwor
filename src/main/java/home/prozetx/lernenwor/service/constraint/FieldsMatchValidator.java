package home.prozetx.lernenwor.service.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.PropertyAccessorFactory;
import java.lang.annotation.Annotation;

public abstract class FieldsMatchValidator<T extends Annotation> implements ConstraintValidator<T, Object> {
    protected String firstFieldName;
    protected String secondFieldName;
    protected String message;

    protected abstract boolean fieldsEqual(Object obj1, Object obj2);

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        final var beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(o);
        final var firstObj = beanWrapper.getPropertyValue(firstFieldName);
        final var secondObj = beanWrapper.getPropertyValue(secondFieldName);

        boolean isValid = fieldsEqual(firstObj, secondObj);
        if (!isValid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext. buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(secondFieldName)
                    .addConstraintViolation();
        }
        return isValid;
    }
}
