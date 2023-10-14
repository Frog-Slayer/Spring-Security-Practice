package frogslayer.auth.member.exception.exceptions;

import frogslayer.auth.member.exception.ExceptionCode;

public class InvalidEmailFormatException extends RuntimeException{
    public InvalidEmailFormatException(){
        super(ExceptionCode.INVALID_EMAIL_FORMAT_EXCEPTION.getMessage());
    }

    public InvalidEmailFormatException(String message){
        super(message);
    }
}
