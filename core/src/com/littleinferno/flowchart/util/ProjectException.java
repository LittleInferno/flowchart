package com.littleinferno.flowchart.util;

public class ProjectException extends RuntimeException {

    public ProjectException(String s) {
        super(s);
    }

    public static <T> ProjectException unknownCodeGenerator(Class<T> type) {
        return new ProjectException("unknown code generator" + type.getSimpleName());
    }
}
