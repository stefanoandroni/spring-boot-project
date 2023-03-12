package org.stand.springbootproject.error.handler;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.stand.springbootproject.dto.response.ErrorBaseResponseBody;
import org.stand.springbootproject.error.*;

import java.util.*;

@ControllerAdvice
@ResponseStatus
@RequiredArgsConstructor
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger LOG = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);
    private final MessageSource messageSource;

    // TODO: implement with a custom AccessDeniedHandler
    /*
        Exception: AccessDeniedException
    */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(
            AccessDeniedException exception,
            WebRequest request
    ) {
        ErrorBaseResponseBody responseBody = new ErrorBaseResponseBody(
                HttpStatus.FORBIDDEN,
                messageSource.getMessage("user.access.error.denied", null, Locale.getDefault())
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
    }

    /*
        Exception: BadCredentialsException
    */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBaseException(
            BadCredentialsException exception,
            WebRequest request
    ) {
        ErrorBaseResponseBody responseBody = new ErrorBaseResponseBody(
                HttpStatus.BAD_REQUEST,
                messageSource.getMessage("user.authentication.error", null, Locale.getDefault())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }


    /*
        Exception: BaseException
    */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> handleBaseException(
            BaseException exception,
            WebRequest request
    ) {
        String messageCode = null;
        List<Object> messageArgs = new ArrayList<>();

        if (exception.getClass().equals(UserEmailAlreadyTakenException.class)) {
            messageCode = "user.register.error.field.taken";
            messageArgs.add("EMAIL");
        } else if (exception.getClass().equals(UserUsernameAlreadyTakenException.class)) {
            messageCode = "user.register.error.field.taken";
            messageArgs.add("USERNAME");
        } else if (exception.getClass().equals(RoleNotFoundException.class)) {
            messageCode = "user.register.error.not.found";
            messageArgs.add("ROLE");
        } else if (exception.getClass().equals(UserNotFoundException.class)) {
            messageCode = "user.register.error.not.found";
            messageArgs.add("USER");
        } else if (exception.getClass().equals(VerificationTokenExpiredException.class)) {
            messageCode = "user.email.confirmation.error.token.expired";
        } else if (exception.getClass().equals(VerificationTokenNotFoundException.class)) {
            messageCode = "user.register.error.not.found";
            messageArgs.add("VERIFICATION TOKEN");
        } else if (exception.getClass().equals(UserNotEnabledException.class)) {
            messageCode = "user.authentication.error.user.not.enabled";
        }
        assert messageCode != null;
        ErrorBaseResponseBody responseBody = new ErrorBaseResponseBody(
                HttpStatus.BAD_REQUEST,
                messageSource.getMessage(messageCode, messageArgs.toArray(), Locale.getDefault())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    /*
        Exception: MethodArgumentNotValidException
    */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        // MethodArgumentNotValidException to Map<fieldname, List<errormessage>>
        Map<String, List<String>> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            if (!errors.containsKey(fieldName)) {
                errors.put(fieldName, new ArrayList<>());
            }
            errors.get(fieldName).add(errorMessage);
        });

        ErrorBaseResponseBody responseBody = new ErrorBaseResponseBody(
                HttpStatus.BAD_REQUEST,
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    // TODO: not works
    /*
        Exception: ExpiredJwtException
    */
    /*
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(
            ExpiredJwtException exception,
            WebRequest request
    ) {
        // LOG.info("IN handleExpiredJwtException()");
        ErrorBaseResponseBody responseBody = new ErrorBaseResponseBody(
                HttpStatus.UNAUTHORIZED,
                messageSource.getMessage("user.access.error.jwt.expired", null, Locale.getDefault())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
    */
    
    /*
        Exception: Exception (generic exceptions handler)
    */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(
            Exception exception,
            WebRequest request
    ) {
        ErrorBaseResponseBody responseBody = new ErrorBaseResponseBody(
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

}
