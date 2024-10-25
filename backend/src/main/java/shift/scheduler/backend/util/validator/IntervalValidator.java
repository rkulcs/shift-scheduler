package shift.scheduler.backend.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import shift.scheduler.backend.model.period.TimeInterval;

public class IntervalValidator implements ConstraintValidator<Interval, TimeInterval> {

    private boolean areEqualValuesAllowed;

    @Override
    public void initialize(Interval constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        areEqualValuesAllowed = constraintAnnotation.areEqualValuesAllowed();
    }

    @Override
    public boolean isValid(TimeInterval interval, ConstraintValidatorContext constraintValidatorContext) {

        if (interval == null || !interval.areBothEndsNonNull())
            return false;

        int comparisonResult = interval.compareEnds();

        if (areEqualValuesAllowed)
            return comparisonResult >= 0;
        else
            return comparisonResult > 0;
    }
}
