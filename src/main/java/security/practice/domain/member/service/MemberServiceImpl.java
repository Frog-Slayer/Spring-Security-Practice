package security.practice.domain.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import security.practice.domain.member.dto.request.MemberJoinDto;
import security.practice.domain.member.entity.Member;
import security.practice.domain.member.entity.Role;
import security.practice.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Member joinMember(MemberJoinDto memberJoinDto) throws Exception {
        if (isDuplicatedUsername(memberJoinDto.getUsername())) throw new Exception("중복된 이메일입니다.");
        Member member = Member.builder()
                .username(memberJoinDto.getUsername())
                .password(memberJoinDto.getPassword())
                .role(Role.GUEST)
                .build();

        return memberRepository.save(member);
    }

    private boolean isDuplicatedUsername(String username) {
        return memberRepository.existsByUsername(username);
    }
}
