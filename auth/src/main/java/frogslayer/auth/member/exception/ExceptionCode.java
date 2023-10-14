package frogslayer.auth.member.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    NO_SUCH_USER_EXCEPTION(404, "존재하지 않는 회원입니다."),
    DUPLICATE_USER_EXCEPTION(404, "이미 가입된 회원입니다"),
    INVALID_PASSWORD_FORMAT_EXCEPTION(404, "잘못된 비밀번호 형식입니다."),
    INVALID_EMAIL_FORMAT_EXCEPTION(404, "잘못된 이메일 형식입니다.");

    private final Integer HttpStatusCode;
    private final String message;

    ExceptionCode(Integer HttpStatusCode, String message){
        this.HttpStatusCode = HttpStatusCode;
        this.message = message;
    }
}
