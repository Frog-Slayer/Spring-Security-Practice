package security.practice.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import security.practice.domain.member.dto.request.MemberJoinDto;
import security.practice.domain.member.service.MemberService;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/register")
    public void signUp(@RequestBody MemberJoinDto joinDto) throws Exception {
        memberService.joinMember(joinDto);
    }
}
