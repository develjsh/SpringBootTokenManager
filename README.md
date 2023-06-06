# Token 관리 어플리케이션

## 설정
- Spring boot 3.1.0
- Gradle - Groovy
- Java 11
- Jar


## 구현
- Token 신규 생성 API : 신규로 accessToken과 refreshToken 생성하는 API endpoint
    - request : 사용자ID / 사용자명
    - response : accessToken / refreshToken
- Token 조회 API : 유효한 accessToken 토근에 매핑된 사용자 정보 전달해주는 API endpoint
    - request : accessToken
    - response : 사용자ID / 사용자명
- AccessToken 갱신 API : refreshToken으로 새로운 AccessToken 발급 API endpoint
    - request : refreshToken으로
    - response : accessToken / refreshToken으로

## 설계
- accessToken : 만료기간 30분
- refreshToken : 만료기간 1주일