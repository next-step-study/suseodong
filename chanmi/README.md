# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리
* 구현 단계에서는 각 요구사항을 구현하는데 집중한다.
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다.

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답
http://localhost:8080/index.html
* 1단계: Buffered Reader로 요청 정보 출력
    * Buffered Reader와 Writer를 사용하고 close하니 Inputstream도 닫혀서 socket이 닫히는 것 같다.
    * InputStream은 한번만 읽을 수 있다. 따라서 여러 번 읽고 싶으면 따로 저장해야 한다.
    * 그냥 문자열로 작업하는 것보다 클래스를 분리해서 파싱하여 사용하는 편이 편하다.

### 요구사항 2 - get 방식으로 회원가입
http://localhost:8080/user/create?userId=javajigi&password=password&name=JaeSung&email=anytime0224@gmail.com

### 요구사항 3 - post 방식으로 회원가입
http://localhost:8080/user/form.html
* body는 json이나 x-www-form-urlencoded 등의 형식으로 `/n`이 끝에 포함되지 않는 경우가 많기 때문에 eof 에러가 발생할 수 있다.
* 따라서 요청 헤더의 `Content-Length`만큼 읽어야 한다.

### 요구사항 4 - redirect 방식으로 이동
* 브라우저가 리다이렉션을 인식하기 위해서는 `3xx` 상태코드와 함께 헤더의 `Location`에 이동할 url을 포함하여 응답한다.

### 요구사항 5 - cookie
* Cookie
    * 설정: `Set-Cookie: key1=value1`
    * 확인: `Cookie: key1=value1; key2=value2; ...`
* 개발자 도구
    * 쿠키 확인: `개발자 도구` - `네트워크` - `쿠키`
    * 쿠키 삭제: `개발자 도구` - `응용 프로그램` - `쿠키`

### 요구사항 6 - 사용자 목록 출력
http://localhost:8080/user/list
* 그냥 문자열만 반환하면 html에서는 줄바꿈이 일어나지 않는다.

### 요구사항 7 - stylesheet 적용

* html에 link 태그로 css나 js같이 추가 파일이 포함되어 있으면 브라우저에서 추가로 해당 파일을 요청한다.
* 헤더에 해당 파일의 확장자에 맞는 content type으로 요청이 오는데, 맞는 파일과 응답 헤더의 `Content-Type`을 해당 타입으로 명시해주어야 브라우저가 제대로 인식 가능하다.

``` html
<link href="css/bootstrap.min.css" rel="stylesheet">
```

### heroku 서버에 배포 후
* 