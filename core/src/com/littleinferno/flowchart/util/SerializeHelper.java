package com.littleinferno.flowchart.util;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import java.io.IOException;

public class SerializeHelper {

    public static <T extends ClassHandle> Pair readHandle(Json json, JsonValue valueMap) {

        Class nodeType = null;
        ClassHandle handle = null;
        try {
            handle =
                    (ClassHandle) json.readValue(ClassReflection.forName(valueMap.name()), valueMap);

            nodeType = ClassReflection.forName(handle.className);
        } catch (ReflectionException e) {
            e.printStackTrace();
        }
        return new Pair(nodeType, handle);
    }

    public static void writeHandle(Json json, BaseHandle handle) {
        try {
            json.getWriter().name(handle.getClass().getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        json.writeValue(handle);
    }

    static public class Pair {
        public Class type;
        public ClassHandle classHandle;

        Pair(Class nodeType, ClassHandle handle) {
            type = nodeType;
            classHandle = handle;
        }
    }
}
