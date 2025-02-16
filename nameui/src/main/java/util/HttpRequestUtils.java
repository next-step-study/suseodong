package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import constants.HttpMethod;
import http.Header;
import http.RequestLine;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpRequestUtils {
    /**
     * @param
     * queryString은 URL에서 ? 이후에 전달되는 field1=value1&field2=value2 형식임
     * @return
     */
    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&");
    }

    /**
     * @param
     * 쿠키값은 name1=value1; name2=value2 형식임
     * @return
     */
    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, ";");
    }

    public static RequestLine getRequestLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        if(line == null) {
            throw new RuntimeException();
        }

        if (line.split(" ")[1].split("\\?").length == 2) {
            return new RequestLine(line.split(" ")[0], line.split(" ")[1].split("\\?")[0], URLDecoder.decode(line.split(" ")[1].split("\\?")[1], "UTF-8"));
        }

        return new RequestLine(line.split(" ")[0], line.split(" ")[1].split("\\?")[0], "");
    }

    public static Header getHeader(BufferedReader br, String httpMethod) throws IOException {

        Map<String, String> header = new HashMap<>();

        // 다음 줄 부터는 ': ' 형식이므로 while 문 돌리기
        String line = br.readLine();
        boolean isPost = httpMethod.equals(HttpMethod.POST.getMethod());
        while(!"".equals(line)) {
            if (line == null) {
                break;
            }

            String key = parseHeader(line).getKey();
            String value = parseHeader(line).getValue();

            header.put(key, value);
            line = br.readLine();
        }

        if (isPost) {
            String content = URLDecoder.decode(IOUtils.readData(br, Integer.parseInt(header.get("Content-Length"))), "UTF-8");
            header.put("content", content);
        }

        return new Header(header);
    }

    private static Map<String, String> parseValues(String values, String separator) {
        if (Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }

        String[] tokens = values.split(separator);
        return Arrays.stream(tokens).map(t -> getKeyValue(t, "=")).filter(p -> p != null)
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }

    static Pair getKeyValue(String keyValue, String regex) {
        if (Strings.isNullOrEmpty(keyValue)) {
            return null;
        }

        String[] tokens = keyValue.split(regex);
        if (tokens.length != 2) {
            return null;
        }

        return new Pair(tokens[0], tokens[1]);
    }

    public static Pair parseHeader(String header) {
        return getKeyValue(header, ": ");
    }

    public static class Pair {
        String key;
        String value;

        Pair(String key, String value) {
            this.key = key.trim();
            this.value = value.trim();
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((key == null) ? 0 : key.hashCode());
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Pair other = (Pair) obj;
            if (key == null) {
                if (other.key != null)
                    return false;
            } else if (!key.equals(other.key))
                return false;
            if (value == null) {
                if (other.value != null)
                    return false;
            } else if (!value.equals(other.value))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Pair [key=" + key + ", value=" + value + "]";
        }
    }
}
