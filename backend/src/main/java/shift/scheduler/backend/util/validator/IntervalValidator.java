package shift.scheduler.backend.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import shift.scheduler.backend.model.period.TimeInterval;

public class IntervalValidator implements ConstraintValidator<Interval, TimeInterval> {
    @Override
    public void initialize(Interval constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(TimeInterval interval, ConstraintValidatorContext constraintValidatorContext) {
        return interval.getStart() != null
                && interval.getEnd() != null
                && interval.getStart() <= interval.getEnd();
    }
}
