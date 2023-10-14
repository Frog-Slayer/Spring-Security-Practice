package frogslayer.auth.member.exception.exceptions;

import frogslayer.auth.member.exception.ExceptionCode;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(){
        super(ExceptionCode.DUPLICATE_USER_EXCEPTION.getMessage());
    }

    public DuplicateUserException(String message){
        super(message);
    }
}
