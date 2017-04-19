package com.littleinferno.flowchart.function;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.littleinferno.flowchart.FlowchartProject;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.project.ProjectModule;

import java.util.ArrayList;
import java.util.List;

public class AndroidFunctionManager implements ProjectModule {

    private List<AndroidFunction> functions;
    private int counter;
    private FlowchartProject project;

    public AndroidFunctionManager(final FlowchartProject project) {
        this.project = project;
        functions = new ArrayList<>();
        counter = 0;
    }


    public AndroidFunction createFunction(String name) {
        AndroidFunction function = new AndroidFunction(this, name);

        functions.add(function);

        return function;
    }

    public AndroidFunction createFunction() {
        AndroidFunction function = new AndroidFunction(this, "newFun" + counter++);

        functions.add(function);
        return function;
    }

    void removeFunction(AndroidFunction function) {
        function.destroy();

        functions.remove(function);
    }

    public AndroidFunction getFunction(String name) {
        return Stream.of(functions)
                .filter(value -> value.getName().equals(name))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Cannot find codegen with title:\"" + name + "\""));
    }

    public String gen(BaseCodeGenerator builder) {
        return Stream.of(functions)
                .map(function -> function.gen(builder))
                .collect(Collectors.joining());
    }

    public List<AndroidFunction> getFunctions() {
        return functions;
    }

    @Override
    public FlowchartProject getProject() {
        return project;
    }
}
