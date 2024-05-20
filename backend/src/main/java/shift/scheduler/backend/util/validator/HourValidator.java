package shift.scheduler.backend.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MultipleOfFourValidator implements ConstraintValidator<MultipleOfFour, Short> {
    @Override
    public void initialize(MultipleOfFour constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Short aShort, ConstraintValidatorContext constraintValidatorContext) {
        return (aShort % 4 == 0);
    }
}
