package br.ufrgs.inf.pet.dinoapi.exception_handler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.List;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class MethodArgumentNotValidExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Error methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(fieldErrors);
    }

    private Error processFieldErrors(List<FieldError> fieldErrors) {
        return new Error(HttpStatus.BAD_REQUEST, "Error in request body validation.", fieldErrors);
    }

    static class Error {
        private final int status;
        private final String message;
        private List<ErrorItem> fieldErrors;

        Error(HttpStatus status, String message, List<FieldError> fieldErrors) {
            this.status = status.value();
            this.message = message;
            this.fieldErrors = fieldErrors.stream().map(fieldError ->
                new ErrorItem(fieldError.getField(), fieldError.getDefaultMessage())
            ).collect(Collectors.toList());
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public List<ErrorItem> getFieldErrors() {
            return fieldErrors;
        }
    }

    static class ErrorItem {
        private String field;
        private String message;

        ErrorItem(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }
    }
}
