package frogslayer.auth.member.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    NO_SUCH_USER_EXCEPTION(404, "존재하지 않는 회원입니다.");

    private final Integer HttpStatusCode;
    private final String message;

    ExceptionCode(Integer HttpStatusCode, String message){
        this.HttpStatusCode = HttpStatusCode;
        this.message = message;
    }
}
