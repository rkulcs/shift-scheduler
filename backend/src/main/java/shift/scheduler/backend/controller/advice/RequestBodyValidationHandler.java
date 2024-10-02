package shift.scheduler.backend.controller.advice;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

/**
 * Implements a handler method which ensures that all request body validation errors are
 * returned to the user.
 */
@RestControllerAdvice
public class RequestBodyValidationHandler extends ResponseEntityExceptionHandler {

    public static record ResponseBodyWithErrors(List<String> errors) {}

    /**
     * Returns a 400 Bad Request response with a list of validation errors in the response body.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        var errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return ResponseEntity.badRequest().body(new ResponseBodyWithErrors(errors));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        var message = ex.getMessage();

        if (message.contains("not one of the values accepted for Enum class: [MANAGER, EMPLOYEE]"))
            message = "Role must be either 'MANAGER' or 'EMPLOYEE'";

        return ResponseEntity.badRequest().body(new ResponseBodyWithErrors(List.of(message)));
    }
}
