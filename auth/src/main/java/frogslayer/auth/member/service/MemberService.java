package frogslayer.auth.member.service;

import frogslayer.auth.member.entity.Member;
import frogslayer.auth.member.exception.exceptions.DuplicateUserException;
import frogslayer.auth.member.exception.exceptions.InvalidEmailFormatException;
import frogslayer.auth.member.exception.exceptions.InvalidPasswordFormatException;
import frogslayer.auth.member.exception.exceptions.NoSuchUserException;

public interface MemberService {

    Member findUserByEmail(String email) throws NoSuchUserException;
    void signUp(Member signUpInfo) throws InvalidEmailFormatException, InvalidPasswordFormatException, DuplicateUserException;
}

