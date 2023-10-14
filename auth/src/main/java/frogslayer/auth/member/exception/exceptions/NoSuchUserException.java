package frogslayer.auth.member.exception.exceptions;


import frogslayer.auth.member.exception.ExceptionCode;

public class NoSuchUserException extends RuntimeException {
    public NoSuchUserException(){
        super(ExceptionCode.NO_SUCH_USER_EXCEPTION.getMessage());
    }

    public NoSuchUserException(String message){
        super(message);
    }
}
