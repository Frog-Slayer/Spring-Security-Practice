package frogslayer.auth.member.exception;

import frogslayer.auth.member.exception.exceptions.DuplicateUserException;
import frogslayer.auth.member.exception.exceptions.InvalidEmailFormatException;
import frogslayer.auth.member.exception.exceptions.InvalidPasswordFormatException;
import frogslayer.auth.member.exception.exceptions.NoSuchUserException;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(NoSuchUserException.class)
    public ResponseEntity<String> handleNoSuchUserException(NoSuchUserException err){
        return getResponseEntity(ExceptionCode.NO_SUCH_USER_EXCEPTION);
    }

    public static ResponseEntity<String> getResponseEntity(ExceptionCode code){
        return ResponseEntity
                .status(code.getHttpStatusCode())
                .body(code.getMessage());
    }
}
