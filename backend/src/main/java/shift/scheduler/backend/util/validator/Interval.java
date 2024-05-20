package shift.scheduler.backend.util.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Ensures that the startHour of a time interval comes before its endHour, assuming that
 * a shift cannot start and end on different days. The range of valid hours is [0, 24].
 * 0 is midnight as a starting hour, and 24 is midnight as an ending hour.
 */
@Constraint(validatedBy = IntervalValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Interval {

    String message() default "The interval's start hour must be before its end hour";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
