package frogslayer.auth.member.service;

import frogslayer.auth.member.entity.Member;
import frogslayer.auth.member.exception.exceptions.DuplicateUserException;
import frogslayer.auth.member.exception.exceptions.InvalidEmailFormatException;
import frogslayer.auth.member.exception.exceptions.InvalidPasswordFormatException;
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
    public void signUp(Member signUpInfo){
        String email = signUpInfo.getEmail();
        verifyEmailFormat(email);
        checkEmailDuplication(email);
        verifyPasswordFormat(email);

        Member member = Member.builder()
                .email(signUpInfo.getEmail())
                .password(signUpInfo.getPassword())
                .privateData(signUpInfo.getPrivateData())
                .build();

        memberRepository.save(member);
    }

    private void verifyPasswordFormat(String password) throws InvalidPasswordFormatException {
        //TODO
    }

    private void verifyEmailFormat(String email) throws InvalidEmailFormatException {
        //TODO
    }

    private void checkEmailDuplication(String email) throws DuplicateUserException {
        if (memberRepository.existsByEmail(email)) throw new DuplicateUserException();
    }
}
