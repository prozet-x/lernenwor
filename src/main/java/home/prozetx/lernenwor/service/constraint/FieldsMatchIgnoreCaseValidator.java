package home.prozetx.lernenwor.service.constraint;

public class FieldsMatchIgnoreCaseValidator extends FieldsMatchValidator<FieldsMatchIgnoreCase> {
    @Override
    public void initialize(FieldsMatchIgnoreCase constraintAnnotation) {
        firstFieldName = constraintAnnotation.firstField();
        secondFieldName = constraintAnnotation.secondField();
        message = constraintAnnotation.message();
    }
    @Override
    protected boolean fieldsEqual(Object obj1, Object obj2) {
        return obj1.toString().equalsIgnoreCase(obj2.toString());
    }
}
