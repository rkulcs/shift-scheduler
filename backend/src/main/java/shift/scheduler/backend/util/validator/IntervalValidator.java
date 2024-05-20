package shift.scheduler.backend.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import shift.scheduler.backend.model.TimePeriod;

public class IntervalValidator implements ConstraintValidator<Interval, TimePeriod> {
    @Override
    public void initialize(Interval constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(TimePeriod timePeriod, ConstraintValidatorContext constraintValidatorContext) {
        short start = timePeriod.getStartHour();
        short end = timePeriod.getEndHour();

        return start < end;
    }
}
