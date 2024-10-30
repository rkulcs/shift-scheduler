package shift.scheduler.backend.controller.documentation;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.*;

/**
 * Used to ignore the "Authorization" parameter in the endpoints shown in Swagger UI.
 * This is required because the values which are set there are ignored. Instead, users
 * will have to provide their JWTs by clicking on the "Authorize" button above the list
 * of endpoints, and filling out the "Value" field.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Parameter(required = false, hidden = true)
public @interface ExcludeFromDocumentation {
}
