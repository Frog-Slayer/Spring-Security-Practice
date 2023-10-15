# Spring-Security-Practice

## [1] Brief
[velog](https://velog.io/@frog_slayer/series/Spring-Security)에서 구현할 JWT 기반 유저 인증 구현 소스 코드(이메일을 이용한 자체 로그인 + OAuth2를 이용한 로그인)

## [2] Environment & Structure
### Back
Spring Boot, Spring Security, Redis

### Front
React

## [3] 구현 내용 리스트 
### 회원 도메인
- [X] 회원 엔티티 정의
- [ ] 회원 가입을 위한 컨트롤러 구현
- [X] 회원 가입 서비스
  - [X] 이메일을 통한 회원 조회
  - [X] 이메일 검증
  - [X] 패스워드 검증
  - [X] 중복 가입 검증
- [X] 예외 처리
  - [X] 회원 조회 불가 예외
  - [X] 이메일 형식 예외
  - [X] 암호 형식 예외
  - [X] 중복 가입 예외
- [X] 테스트
  - [X] 이메일을 통한 회원 조회 및 중복 가입 테스트 
  - [X] 이메일 검증 테스트
  - [X] 패스워드 검증

### 인증 서비스
- [ ] JWT를 이용한 인증 구현
  - [ ] controller
    - [ ] refresh 요청 처리
  - [ ] config
  - [ ] entity
    - [X] UserDetails 구현
  - [ ] service
    - [X] UserDetailsService 구현
    - [X] JWT 서비스
      - [X] 토큰 발행
      - [X] 토큰에서 회원 정보 추출
      - [X] 토큰 유효성 검사
      - [X] 토큰 보내기
    - [ ] 
  - [ ] filter
    - [ ] Username-Password 인증 필터 (로그인)
    - [ ] JWT 인증 필터 (로그인 후 API 요청)
  - [ ] handler
    - [ ] 로그인 성공 핸들러
    - [ ] 로그인 실패 핸들러
  
- [ ] OAuth2를 이용한 소셜 로그인
  - [ ] entity
    - [ ] DefaultOAuth2User 클래스 확장
  - [ ] dto
    - [ ] 권한 부여 서버 별로 다른 사용자 정보 파싱
  - [ ] handler
    - [ ] OAuth2 로그인 성공 핸들러
    - [ ] OAuth2 로그인 실패 핸들러
  - [ ] service
    - [ ] OAuth2UserService 구현
