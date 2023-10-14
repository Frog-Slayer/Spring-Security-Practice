package frogslayer.auth.member.service;

import frogslayer.auth.AuthApplication;
import frogslayer.auth.member.exception.exceptions.InvalidPasswordFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

//최소 8자 + 최소 한개의 대소문자 + 최소 한개의 숫자 + 최소 한개의 특수 문자
@SpringBootTest
@ContextConfiguration(classes = AuthApplication.class)
class PasswordValidationTest {

    @Autowired
    private MemberServiceImpl memberService;

    @DisplayName("빈 비밀번호 테스트")
    @Test
    void testBlank() {
        String password = "";
        assertThrows(InvalidPasswordFormatException.class, () -> memberService.verifyPasswordFormat(password));
    }

    @DisplayName("8자 미만 테스트")
    @Test
    void testUnder8() {
        String password = "paswo1!";
        assertThrows(InvalidPasswordFormatException.class, () -> memberService.verifyPasswordFormat(password));
    }


    @DisplayName("대소문자 없는 경우 테스트")
    @Test
    void testWithoutAlphabet() {
        String password = "123123!!";
        assertThrows(InvalidPasswordFormatException.class, () -> memberService.verifyPasswordFormat(password));
    }

    @DisplayName("숫자 없는 경우 테스트")
    @Test
    void testWithoutNumber() {
        String password = "password!";
        assertThrows(InvalidPasswordFormatException.class, () -> memberService.verifyPasswordFormat(password));
    }

    @DisplayName("특수문자 없는 경우 테스트")
    @Test
    void testWithoutSpecialCharacter() {
        String password = "password1";
        assertThrows(InvalidPasswordFormatException.class, () -> memberService.verifyPasswordFormat(password));
    }

    @DisplayName("통과 사례")
    @Test
    void testValidPassword() {
        String password = "password1!";
        assertDoesNotThrow(() -> memberService.verifyPasswordFormat(password));
    }

}
