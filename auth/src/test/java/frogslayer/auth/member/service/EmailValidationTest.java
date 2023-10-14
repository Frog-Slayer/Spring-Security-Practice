package frogslayer.auth.member.service;

import frogslayer.auth.AuthApplication;
import frogslayer.auth.member.exception.exceptions.InvalidEmailFormatException;
import frogslayer.auth.member.exception.exceptions.InvalidPasswordFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = AuthApplication.class)
class EmailValidationTest {

    @Autowired
    private MemberServiceImpl memberService;
    @DisplayName("빈 이메일로 가입 시도")
    @Test
    void testEmailValidationWithBlank() {
        String email = "";
        assertThrows(InvalidEmailFormatException.class, () -> memberService.verifyEmailFormat(email));
    }

    @DisplayName("도메인 없는 이메일 가입 시도 테스트")
    @Test
    void testEmailValidationWithoutEmailDomain() {
        String email = "email";
        assertThrows(InvalidPasswordFormatException.class, () -> memberService.verifyEmailFormat(email));
    }

    @DisplayName("최상위 도메인이 없는 이메일 가입 시도 테스트")
    @Test
    void testEmailValidationWithoutTLD() {
        String email = "email@dom";
        assertThrows(InvalidEmailFormatException.class, () -> memberService.verifyEmailFormat(email));
    }

    @DisplayName("최상위 도메인만 있는 이메일 가입 시도 테스트")
    @Test
    void testEmailValidationOnlyWithTLD() {
        String email = "email@.com";
        assertThrows(InvalidEmailFormatException.class, () -> memberService.verifyEmailFormat(email));
    }

    @DisplayName(". 뒤에 아무 것도 없는 경우 테스트")
    @Test
    void testEmailValidationNothingAfterDot() {
        String email = "email@dom.";
        assertThrows(InvalidEmailFormatException.class, () -> memberService.verifyEmailFormat(email));
    }

    @DisplayName("도메인만 있는 경우 테스트")
    @Test
    void testEmailValidationOnlyWithDomain() {
        String email = "@dom.com";
        assertThrows(InvalidEmailFormatException.class, () -> memberService.verifyEmailFormat(email));
    }

    @DisplayName("@가 두 개 이상 있는 경우 테스트")
    @Test
    void testEmailValidationWithMultipleAtS() {
        String email = "user@name@dom.com";
        assertThrows(InvalidEmailFormatException.class, () -> memberService.verifyEmailFormat(email));
    }

    @DisplayName(".이 두 개 이상 있는 경우 테스트")
    @Test
    void testEmailValidationWithMultipleDots() {
        String email = "username@dom.co.m";
        assertThrows(InvalidEmailFormatException.class, () -> memberService.verifyEmailFormat(email));
    }


    @DisplayName("제대로 된 포맷임에도 예외가 발생하지 않는지 테스트")
    @Test
    void testWithValidEmailFormat(){
        String email = "username@domain.com";
        assertDoesNotThrow(() -> memberService.verifyEmailFormat(email));
    }
}