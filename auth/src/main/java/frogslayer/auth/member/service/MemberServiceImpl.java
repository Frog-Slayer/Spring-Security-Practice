package frogslayer.auth.member.service;

import frogslayer.auth.member.entity.Member;
import frogslayer.auth.member.exception.exceptions.DuplicateUserException;
import frogslayer.auth.member.exception.exceptions.InvalidEmailFormatException;
import frogslayer.auth.member.exception.exceptions.InvalidPasswordFormatException;
import frogslayer.auth.member.exception.exceptions.NoSuchUserException;
import frogslayer.auth.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member findUserByEmail(String email) throws NoSuchUserException {
        return memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchUserException());
    }

    @Override
    public void signUp(Member signUpInfo) throws InvalidEmailFormatException, InvalidPasswordFormatException, DuplicateUserException{
        String email = signUpInfo.getEmail();
        String password = signUpInfo.getPassword();
        verifyEmailFormat(email);
        checkEmailDuplication(email);
        verifyPasswordFormat(password);

        Member member = Member.builder()
                .email(signUpInfo.getEmail())
                .password(signUpInfo.getPassword())
                .privateData(signUpInfo.getPrivateData())
                .build();

        memberRepository.save(member);
    }

    //최소 8자 + 최소 한개의 대소문자 + 최소 한개의 숫자 + 최소 한개의 특수 문자
    public void verifyPasswordFormat(String password) throws InvalidPasswordFormatException {
        String regex =  "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        if (!password.matches(regex)) throw new InvalidPasswordFormatException();
    }

    public void verifyEmailFormat(String email) throws InvalidEmailFormatException {
        if (email.isBlank()) throw new InvalidEmailFormatException();
        if (!EmailValidator.getInstance().isValid(email)) throw new InvalidEmailFormatException();
    }

    public void checkEmailDuplication(String email) throws DuplicateUserException {
        if (memberRepository.existsByEmail(email)) throw new DuplicateUserException();
    }
}
