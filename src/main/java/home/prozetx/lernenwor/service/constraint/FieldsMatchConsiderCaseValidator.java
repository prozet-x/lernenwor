package home.prozetx.lernenwor.service.constraint;

import java.util.Objects;

public class FieldsMatchConsiderCaseValidator extends FieldsMatchValidator<FieldsMatchConsiderCase> {
    @Override
    public void initialize(FieldsMatchConsiderCase constraintAnnotation) {
        firstFieldName = constraintAnnotation.firstField();
        secondFieldName = constraintAnnotation.secondField();
        message = constraintAnnotation.message();
    }
    @Override
    protected boolean fieldsEqual(Object obj1, Object obj2) {
        return Objects.equals(obj1.toString(), obj2.toString());
    }
}
