package org.grp2.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Context;
import io.javalin.core.util.ContextUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JavalinTestUtility {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static Context getContext(String path, Map<String, String> queryParams, String body, Map<String, String> pathParams) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        request.setQueryString(makeQueryString(queryParams));
        if (body != null) {
            request.setReader(body);
        }

        if (pathParams != null) {
            return ContextUtil.init(request, response, path, pathParams);
        } else {
            return ContextUtil.init(request, response, path);
        }
    }

    public static Context getContext(String path, String... pathParamKeyValuePairs) {
        Map<String, String> pathParams = makePathParamMap(pathParamKeyValuePairs);
        return getContext(path, null, null, pathParams);
    }

    public static Context getContext(String path, Map<String, String> queryParams, String... pathParamKeyValuePairs) {
        Map<String, String> pathParams = makePathParamMap(pathParamKeyValuePairs);
        return getContext(path, queryParams, null, pathParams);
    }

    public static Map<String, Object> getResponse(Context context) {
        Map<String, Object> response = null;
        try {
            response = objectMapper.readValue(context.resultString(), new TypeReference<Map<String,Object>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static <T> T getResponse(Context context, Class<T> type) {
        T response = null;
        try {
            response = objectMapper.readValue(context.resultString(), type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static <T> T getResponse(Context context, TypeReference typeReference) {
        T response = null;
        try {
            response = objectMapper.readValue(context.resultString(), typeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static Map<String, String> makePathParamMap(String... keyValuePairs) {
        if (keyValuePairs.length > 0 && keyValuePairs.length % 2 != 0) {
            throw new IllegalArgumentException("Key Value Pairs must have an even number of elements, otherwise you cannot create a map of it");
        }
        if (keyValuePairs.length == 0) {
            return null;
        }

        Map<String, String> map = new HashMap<>();

        for (int i = 0; i < keyValuePairs.length-1; i = i+2) {
            String key = keyValuePairs[i];
            String value = keyValuePairs[i+1];
            map.put(key, value);
        }

        return map;
    }

    private static String makeQueryString(Map<String, String> queryParams) {
        StringBuilder sb = new StringBuilder();

        if (queryParams != null) {
            boolean first = true;
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue());
                if (first && queryParams.size() > 1) {
                    sb.append("&");
                    first = false;
                }
            }
        }

        return sb.toString();
    }
}
