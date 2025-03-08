package http.filter;

import http.request.Request;
import http.response.Response;

public interface Filter {
    void doFilter(Request request, Response response, FilterChain chain);
}
