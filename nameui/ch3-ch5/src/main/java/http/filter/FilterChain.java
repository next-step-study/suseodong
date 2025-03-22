package http.filter;

import http.request.Request;
import http.response.Response;

import java.util.ArrayList;
import java.util.List;

public class FilterChain {
    private List<Filter> filters = new ArrayList<>();
    private int index = 0;

    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    public void doFilter(Request request, Response response) {
        if (index < filters.size()) {
            filters.get(index++).doFilter(request, response, this);
        }
//        for (ResourceFilter filter : filters) {
//            filter.doFilter(request, response, this);
//        }
    }
}

