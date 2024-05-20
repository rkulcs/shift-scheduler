package shift.scheduler.backend.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HourValidator implements ConstraintValidator<Hour, Short> {
    @Override
    public void initialize(Hour constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Short aShort, ConstraintValidatorContext constraintValidatorContext) {
        return (0 <= aShort && aShort <= 24 && (aShort % 4 == 0));
    }
}
