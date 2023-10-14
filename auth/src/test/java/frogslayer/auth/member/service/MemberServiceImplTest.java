package frogslayer.auth.member.service;

import frogslayer.auth.AuthApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = AuthApplication.class)
class MemberServiceImplTest {

    @Autowired
    private MemberServiceImpl memberService;
    @DisplayName("빈 이메일로 가입 시도")
    @Test
    void testEmailValidationWithBlank() {
        String email = "";
        assertThrows(Exception.class, () -> memberService.verifyEmailFormat(email));
    }


}