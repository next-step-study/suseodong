package controller;

import controller.adapter.ControllerHandlerAdapter;
import controller.adapter.HandlerAdapter;
import controller.etc.CssController;
import controller.etc.HtmlController;
import controller.home.HomeController;
import controller.user.UserListController;
import controller.user.UserLoginController;
import controller.user.UserSaveController;
import http.request.Request;
import http.response.HttpResponse;
import http.response.Response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrontController {
    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    // 핸들러 매핑과 어댑터 초기화(등록)
    public FrontController() {
        initHandlerMappingMap(); // 핸들러 매핑 초기화
        initHandlerAdapters(); // 어댑터 초기화
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/user/login", new UserLoginController());
        handlerMappingMap.put("/user/create", new UserSaveController());
        handlerMappingMap.put("/user/list", new UserListController());
        handlerMappingMap.put("/", new HomeController());
        handlerMappingMap.put("/css", new CssController());
        handlerMappingMap.put("/html", new HtmlController());
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerHandlerAdapter());
    }

    public void service(Request request, Response response) throws IOException {
        Object handler = getHandler(request);

        if (handler == null) {
            throw new RuntimeException("잘못된 요청입니다.");
        }

        // 어댑터 찾기
        HandlerAdapter adapter = getHandlerAdapter(handler);

        // 어댑터 호출 : 여기서 로직 모두 수행되는 것
        adapter.handle(request, response, handler);
    }

    // 핸들러 매핑 : URL 에 매핑된 핸들러(컨트롤러) 객체 반환
    private Object getHandler(Request request) {
        String requestURI = request.getRequestURI();
        if (handlerMappingMap.get(requestURI) != null) {
            return handlerMappingMap.get(requestURI);
        }
        else if (requestURI.endsWith(".css")) { // css 처리
            return handlerMappingMap.get("/css");
        }
        return handlerMappingMap.get("/html"); // 기본 html 처리
    }

    // 핸들러(컨트롤러) 처리 가능한 어댑터 조회
    private HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }

        throw new IllegalArgumentException("handler adapter 를 찾을 수 없습니다. handler = " + handler);
    }
}
