package frogslayer.auth.member.service;

import frogslayer.auth.AuthApplication;
import frogslayer.auth.member.entity.Member;
import frogslayer.auth.member.exception.exceptions.DuplicateUserException;
import frogslayer.auth.member.exception.exceptions.InvalidEmailFormatException;
import frogslayer.auth.member.exception.exceptions.InvalidPasswordFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ContextConfiguration(classes = AuthApplication.class)
class SignUpTest {

    @Autowired
    private MemberServiceImpl memberService;

    @DisplayName("가입")
    @Test
    void signUpTest(){
        String email = "username@domain.com";
        String password = "password1!";

        Member member = Member.builder()
                        .email(email)
                        .password(password)
                        .build();

        assertDoesNotThrow(() -> memberService.signUp(member));
    }

    @DisplayName("중복 가입 테스트")
    @Test
    void duplicateSignUpTest(){
        signUpTest();
        String email = "username@domain.com";
        String password = "password1!";

        Member member = Member.builder()
                .email(email)
                .password(password)
                .build();

        assertThrows(DuplicateUserException.class, () -> memberService.signUp(member));
    }

    @DisplayName("성공 사례 - 중복되지 않은 가입")
    @Test
    void testWithMultipleUser(){
        signUpTest();
        String email = "username2@domain.com";
        String password = "password1!";

        Member member = Member.builder()
                .email(email)
                .password(password)
                .build();

        assertDoesNotThrow(() -> memberService.signUp(member));
    }
}