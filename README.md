# Token 관리 어플리케이션
JWT token을 활용하는 프로젝트입니다.
따로 DB에 token 정보를 저장하지 않으며, token에 사용자 ID와 사용자명을 저장하여 만료되지 않으면 사용할 수 있습니다.

**설정**
- Spring boot 2.7.1
- Gradle - Groovy
- Java 17
- Jar

**구현**
- Token 신규 생성 API : 신규로 accessToken과 refreshToken 생성하는 API endpoint
    - request : 사용자ID / 사용자명
    - response : accessToken / refreshToken
- Token 조회 API : 유효한 accessToken 토근에 매핑된 사용자 정보 전달해주는 API endpoint
    - request : accessToken
    - response : 사용자ID / 사용자명
- AccessToken 갱신 API : refreshToken으로 새로운 AccessToken 발급 API endpoint
    - request : refreshToken으로
    - response : accessToken / refreshToken

**response는 ResponseDto 오버로딩을 통해 상황에 맞게 보낼 수 있도록 구현했습니다.**

**조건**
- accessToken : 만료기간 30분
- refreshToken : 만료기간 1주일

## 프로젝트 설치 및 실행 방법
Github를 통해 파일을 다운받아 IDE에서 실행을 해줍니다.

## 테스트
### 로컬 호스트
- Token 신규 생성 API : POST http://localhost:8080/api/create/token
    - body에 JSON 형태로 사용자ID와 사용자명 담아 호출합니다.

**예시**
{
    "userId": 조선",
    "username": "홍길동"
}

    - /api/create/**는 WebSecurityConfig에서 SecurityFilterChain에서 제외시켜두어 token이 없는 상태에서도  호출이 가능합니다.

- Token 조회 API : GET http://localhost:8080/api/search/userInfo
Headers에 Access_Token과 Refresh_Token 값을 담아 호출하면 해당 token에 사용자ID와 사용자명 값을 가져와 return 합니다.

- AccessToken 갱신 API : PATCH http://localhost:8080/api/patch/token
Headers에 Refresh_Token 값을 담아 호출하면 Refresh_Token 값을 통해 새로운 Access_Token을 발급합니다.