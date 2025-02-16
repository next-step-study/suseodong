package util;

import com.google.common.base.Strings;
import java.util.HashMap;
import java.util.Map;
import util.http.ReqHeader;

public class HttpRequestUrlUtils {

    public static String parseRequestMethod(String line) {
        if (!Strings.isNullOrEmpty(line)) {
            String[] firstLine = line.split(" ");
            if (!firstLine[0].isEmpty()) return firstLine[0];
        }
        return "";
    }

    public static String parseRequestUrl(String line) {
        if (!Strings.isNullOrEmpty(line)) {
            String[] firstLine = line.split(" ");
            if (!firstLine[1].isEmpty()) return firstLine[1];
        }
        return "";
    }

    /**
     * @param header는
     *            Request Header에서 첫 번째 줄 이후로 전달되는 field1: value\nfield2: value2 형식임
     * @return
     */
    public static ReqHeader parseRequestHeader(String header) {
        if (validateLine(header, "\n")) {
            return new ReqHeader(new HashMap<>());
        }
        return new ReqHeader(parseLines(header));
    }

    private static Map<String, String> parseLines(String header) {
        HashMap<String, String> parsedHeader = new HashMap<>();

        String[] lines = header.split("\n");
        for (String line : lines) {
            if (validateLine(line, ": ")) return new HashMap<>();

            String[] tokens = line.split(": ");
            parsedHeader.put(tokens[0], tokens[1]);
        }
        return parsedHeader;
    }

    private static boolean validateLine(String line, String delimiter) {
        return Strings.isNullOrEmpty(line) || !line.contains(delimiter);
    }

}
