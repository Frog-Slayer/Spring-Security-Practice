package security.practice.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import security.practice.domain.member.dto.request.MemberJoinDto;
import security.practice.domain.member.service.MemberService;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private static final Logger log = LoggerFactory.getLogger(MemberController.class);
    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<Void> signUp(@RequestBody MemberJoinDto joinDto) throws Exception {
        memberService.joinMember(joinDto);
        return new ResponseEntity<>(HttpStatusCode.valueOf(201));
    }

    @GetMapping("/test")
    public ResponseEntity<String> authenticatedApi() {
        log.info("hello");
        return new ResponseEntity<>("hello", HttpStatusCode.valueOf(200));
    }
}
