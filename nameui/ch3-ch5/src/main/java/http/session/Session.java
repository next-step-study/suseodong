package http.session;

public interface Session {
    /**
     * 현재 세션에 할당되어 있는 고유한 세션 아이디를 반환
     * @return 세션 아이디
     */
    String getId();

    /**
     * 현재 세션에 value 인자로 전달되는 객체를 name 인자 이름으로 저장
     * @param name 값 이름
     * @param value 저장할 값
     */
    void setAttribute(String name, Object value);

    /**
     * 현재 세션에 name 인자로 저장되어 있는 객체 값을 찾아 반환
     * @param name 찾을 객체 이름
     * @return name 으로 찾은 객체
     */
    Object getAttribute(String name);

    /**
     * 현재 세션에 name 인자로 저장되어 있는 객체 값을 삭젠
     * @param name 지울 객체 이름
     */
    void removeAttribute(String name);

    /**
     * 현재 세션에 name 인자로 저장되어 있는 객체 값을 삭제
     */
    void invalidate();
}
