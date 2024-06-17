package security.practice.global.auth.service.jwt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtServiceImplTest {

    @Autowired
    private JwtService jwtService;

    @Test
    void extractName() {
        String username = "pj0642@gmail.com";
        String accessToken = jwtService.generateAccessToken(username);
        assertEquals(username, jwtService.extractName(accessToken).get());
    }

    @Test
    void validateToken() {
    }
}