package frogslayer.auth.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import frogslayer.auth.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    private String secretKey = "asd19i23hj909sfua9s0fdusd90fua9023fjnh";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    private Integer accessTokenExpiration = 1000 * 60 * 60 * 30;//30분
    private Integer refreshTokenExpiration = 1000 * 60 * 60 * 60 * 24 * 7; //1주일

    public String issueAccessToken(String username){
        Date now = new Date();
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + accessTokenExpiration))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public Optional<String> extractAccessToken(HttpServletRequest request){
        return Optional.ofNullable(request.getHeader("Authorization"))
                .filter(token -> token.startsWith("Bearer"))
                .map(token -> token.replace("Bearer", ""));
    }


    public Optional<String> extractNameFromToken(String token){
        return Optional.ofNullable(JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token)
                .getClaim("sub")
                .asString());
    }

    public String issueRefreshToken(String username){
        return UUID.randomUUID().toString();
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request){
        return Optional.ofNullable(request.getCookies())
                .flatMap(cookies -> Arrays.stream(cookies)
                        .filter(e -> e.getName().equals(REFRESH_TOKEN_COOKIE_NAME))
                        .findAny())
                .map(token -> token.getValue());
    }

    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken){
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
    }
    public void saveRefreshToken(String username, String refreshToken){
        //TODO Redis에 리프레시 토큰을 담아서 관리할 것.


    }


}
