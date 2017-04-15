package com.littleinferno.flowchart.function;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.project.Project;
import com.littleinferno.flowchart.util.managers.ProjectManager;

import java.util.ArrayList;

public class AndroidFunctionManager extends ProjectManager {

    private ArrayList<AndroidFunction> functions;
    private int counter;

    public AndroidFunctionManager(Project project) {
        super(project);
        functions = new ArrayList<>();
        counter = 0;
    }


    private AndroidFunction createFunction(String name) {
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

    @Override
    public void dispose() {

    }
}
