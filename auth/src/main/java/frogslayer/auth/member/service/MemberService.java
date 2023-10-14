package frogslayer.auth.member.service;

import frogslayer.auth.member.entity.Member;
import frogslayer.auth.member.exception.exceptions.NoSuchUserException;

import java.util.Optional;

public interface MemberService {

    Member findUserByEmail(String email) throws NoSuchUserException;
    void signUp(Member signUpInfo);

}

