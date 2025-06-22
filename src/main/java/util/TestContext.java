package util;

import static constant.Constants.API_KEY;

import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

public class TestContext {
    // Each thread starts with its own new HashMap
    // Prevents test data bleeding between parallel threads
    // Perfect for storing data like api keys, auth tokens, user info, responses, etc.
    private static final ThreadLocal<Map<String, Object>> context = ThreadLocal.withInitial(HashMap::new);

    public static void set(String key, Object value) {
        context.get().put(key, value);
    }

    public static Object get(String key) {
        return context.get().get(key);
    }

    public static String getAsString(String key) {
        Object value = get(key);
        return value != null ? value.toString() : null;
    }

    public static void clear() {
        context.remove(); //Clear ThreadLocal after every scenario
    }

    // Convenience methods for common keys
    public static void setApiKey(String key) {
        set(API_KEY, key);
    }

    public static String getApiKey() {
        return getAsString(API_KEY);
    }

    public static void setResponse(Response response) {
        set("response", response);
    }

    public static Response getResponse() {
        return (Response) get("response");
    }
}