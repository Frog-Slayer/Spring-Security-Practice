# Spring-Security-Practice
A repository for JWT authentication using spring security

## To-Do's
+ [ ] JWT를 이용한 로그인
  + [ ] Member 
    + [ ] Entity
    + [ ] Controller
    + [ ] DTO
      + [ ] 회원가입 DTO
      + [ ] 로그인 DTO
    + [ ] Service
      + [ ] Interface
        + [ ] 회원가입
          + [ ] 이메일 인증
          + [ ] DB에 회원 정보를 추가
        + [ ] 중복확인
      + [ ] Implementation
    
  + [ ] Security
    + [ ] SecurityConfig
    + [ ] UserDetails
    + [ ] JwtService
      + [ ] AccessToken
        + [ ] 발급
        + [ ] 추출
        + [ ] 검증
      + [ ] RefreshToken
        + [ ] 발급
        + [ ] 추출
        + [ ] 검증
    + [ ] JwtAuthenticationFilter
    + [ ] Controller
      + [ ] 리프레시 요청 API
    + [ ] Exceptions
    
+ [ ] OAuth2를 통한 로그인 - JWT를 이용한 로그인 구현 후 추가 