package frogslayer.auth.member.controller;

import frogslayer.auth.member.dto.SignUpRequestDTO;
import frogslayer.auth.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    @PostMapping("/signup")
    public ResponseEntity<String> signup(SignUpRequestDTO signupRequest){
        memberService.signUp(SignUpRequestDTO.toEntity(signupRequest));
        return new ResponseEntity<>("회원 가입에 성공함", HttpStatus.CREATED);
    }
}
