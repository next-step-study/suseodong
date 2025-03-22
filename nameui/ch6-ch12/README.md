## ch6 트러블 슈팅
```
org.apache.jasper.jasperexception: the absolute uri: http://java.sun.com/jsp/jstl/core cannot be resolved in either web.xml or the jar files deployed with this application
java.lang.ClassNotFoundException: org.apache.jsp.user.list_jsp
```

- jstl 을 찾을 수 없다는 오류가 지속적으로 발생
- 이유를 찾아보니, 내 프로젝트 자바 버전이 17 로 되어 있어서 났던 오류
- java 8 로 바꾸어주니 정상적으로 작동했다!

### 개인정보수정
- 기존 userId 를 어떻게 넘겨줘야 할지 모르겠어서, 쿼리 스트링으로 전송함. 추후에 리팩터링해야함