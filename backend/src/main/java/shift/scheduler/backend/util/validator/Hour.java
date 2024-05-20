package shift.scheduler.backend.util.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Ensures that a time period's start and end hours are valid. These hours can only be
 * multiples of four in the range of [0, 24].
 */
@Constraint(validatedBy = HourValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Hour {

    String message() default "Number must be a multiple of four";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
