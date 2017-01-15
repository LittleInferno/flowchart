package com.littleinferno.flowchart.codegen;

import java.util.HashMap;
import java.util.Map;

public class Functions {

    private static final Map<String, Function> functions;
    static {
        functions = new HashMap<String, Function>();
    }

    public static void clear() {
        functions.clear();
    }

    public static Map<String, Function> getFunctions() {
        return functions;
    }

    public static boolean isExists(String key) {
        return functions.containsKey(key);
    }

    public static Function get(String key) {
        if (!isExists(key)) throw new RuntimeException(key);
        return functions.get(key);
    }

    public static void set(String key, Function function) {
        functions.put(key, function);
    }
}
