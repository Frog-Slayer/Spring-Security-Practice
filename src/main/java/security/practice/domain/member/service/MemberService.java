package security.practice.domain.member.service;

import org.springframework.stereotype.Service;
import security.practice.domain.member.dto.request.MemberJoinDto;
import security.practice.domain.member.entity.Member;

@Service
public interface MemberService {

    Member joinMember(MemberJoinDto memberJoinDto) throws Exception;
    boolean isDuplicatedUsername(String username);
}
