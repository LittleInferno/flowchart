package com.littleinferno.flowchart.codegen;

public interface BaseJSEngine {

    void init();

    void addMethod(final Object object, final String methodName, final String jsFunctionName, final Class<?>[] parameterTypes);

}
