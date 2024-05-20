package shift.scheduler.backend.util.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = MultipleOfFourValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MultipleOfFour {

    String message() default "Number must be a multiple of four";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
