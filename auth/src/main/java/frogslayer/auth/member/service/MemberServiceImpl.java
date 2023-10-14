package frogslayer.auth.member.service;

import frogslayer.auth.member.entity.Member;
import frogslayer.auth.member.exception.exceptions.NoSuchUserException;
import frogslayer.auth.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member findUserByEmail(String email) throws NoSuchUserException {
        return memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchUserException());
    }

    @Override
    public void signUp(Member signUpInfo) {

    }
}
