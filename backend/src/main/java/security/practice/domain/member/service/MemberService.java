package security.practice.domain.member.service;

import org.springframework.stereotype.Service;
import security.practice.domain.member.dto.request.MemberJoinDto;
import security.practice.domain.member.entity.Member;

public interface MemberService {

    Member joinMember(MemberJoinDto memberJoinDto) throws Exception;

    //이메일 인증 메서드도 추가해야 함
}
