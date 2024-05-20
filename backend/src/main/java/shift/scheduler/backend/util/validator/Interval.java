package shift.scheduler.backend.util.validator;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Constraint(validatedBy = ValidIntervalValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidInterval {
}
