package com.littleinferno.flowchart.function;

import android.os.Parcel;
import android.os.Parcelable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.project.FlowchartProject;
import com.littleinferno.flowchart.project.ProjectModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AndroidFunctionManager implements ProjectModule, Parcelable {

    public static final String TAG = "FUNCTION_MANAGER";

    private List<AndroidFunction> functions;
    private int counter;
    private FlowchartProject project;

    public AndroidFunctionManager(final FlowchartProject project) {
        this.project = project;
        functions = new ArrayList<>();
        counter = 0;
    }

    protected AndroidFunctionManager(Parcel in) {
        counter = in.readInt();
    }

    public static final Creator<AndroidFunctionManager> CREATOR = new Creator<AndroidFunctionManager>() {
        @Override
        public AndroidFunctionManager createFromParcel(Parcel in) {
            return new AndroidFunctionManager(in);
        }

        @Override
        public AndroidFunctionManager[] newArray(int size) {
            return new AndroidFunctionManager[size];
        }
    };

    public AndroidFunction createFunction(String name) {
        AndroidFunction function = new AndroidFunction(this, name);

        functions.add(function);

        return function;
    }

    public MainFunction createMain() {
        MainFunction function = new MainFunction(this);

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

    public String checkFunctionName(final String name) {

        if (name.isEmpty()) {
            return "Name can not be empty";
        } else if (!project.getPluginManager().getCodeGenerator().checkPattern(name)) {
            return "Unacceptable symbols";
        } else if (Stream.of(functions).map(AndroidFunction::getName).anyMatch(name::equals)) {
            return "This name is already taken";
        } else if (project.getPluginManager().getCodeGenerator().containsWord(name))
            return "Invalid name";

        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(counter);
        dest.writeList(functions);
        dest.writeList(Collections.singletonList(getProject()));//TODO action hack
    }
}
//"[$a-zA-Z_][0-9a-zA-Z_$]*"