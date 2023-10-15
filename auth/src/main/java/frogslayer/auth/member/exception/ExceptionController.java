package frogslayer.auth.member.exception;

import frogslayer.auth.member.exception.exceptions.DuplicateUserException;
import frogslayer.auth.member.exception.exceptions.InvalidEmailFormatException;
import frogslayer.auth.member.exception.exceptions.InvalidPasswordFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("frogslayer.auth")
public class ExceptionController {
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<String> handleDuplicateUserException(DuplicateUserException err){
        return getResponseEntity(ExceptionCode.DUPLICATE_USER_EXCEPTION);
    }

    @ExceptionHandler(InvalidEmailFormatException.class)
    public ResponseEntity<String>  handleInvalidEmailFormatException(InvalidEmailFormatException err){
        return getResponseEntity(ExceptionCode.INVALID_EMAIL_FORMAT_EXCEPTION);
    }

    @ExceptionHandler(InvalidPasswordFormatException.class)
    public ResponseEntity<String> handleInvalidPasswordFormatException(InvalidPasswordFormatException err){
        return getResponseEntity(ExceptionCode.INVALID_PASSWORD_FORMAT_EXCEPTION);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException err){
        return getResponseEntity(ExceptionCode.USERNAME_NOT_FOUND_EXCEPTION);
    }

    public static ResponseEntity<String> getResponseEntity(ExceptionCode code){
        return ResponseEntity
                .status(code.getHttpStatusCode())
                .body(code.getMessage());
    }
}
