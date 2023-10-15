package frogslayer.auth.member.service;

import frogslayer.auth.member.entity.Member;
import frogslayer.auth.member.exception.exceptions.DuplicateUserException;
import frogslayer.auth.member.exception.exceptions.InvalidEmailFormatException;
import frogslayer.auth.member.exception.exceptions.InvalidPasswordFormatException;

public interface MemberService {

    Member findUserByEmail(String email) throws NoSuchUserException;
    void signUp(Member signUpInfo) throws InvalidEmailFormatException, InvalidPasswordFormatException, DuplicateUserException;

    void verifyPasswordFormat(String password) throws InvalidPasswordFormatException;
    void verifyEmailFormat(String email) throws InvalidEmailFormatException;
    void checkEmailDuplication(String email) throws DuplicateUserException;
}

