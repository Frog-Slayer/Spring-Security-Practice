package frogslayer.auth.member.exception.exceptions;

import frogslayer.auth.member.exception.ExceptionCode;

public class InvalidPasswordFormatException extends RuntimeException{
    public InvalidPasswordFormatException(){
        super(ExceptionCode.INVALID_PASSWORD_FORMAT_EXCEPTION.getMessage());
    }

    public InvalidPasswordFormatException(String message){
        super(message);
    }
}
