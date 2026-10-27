package in.harmeetsingh.springaiplayground._02_Prompt.exception_handlers;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(
            MethodArgumentNotValidException ex
    ) {
        var problemDetails = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "validation failed"
        );
        var validationMessages = ex.getBindingResult().getAllErrors()
                .stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .toList();

        problemDetails.setProperty("validationErrors", validationMessages);
        return problemDetails;
    }
}
