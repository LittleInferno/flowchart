package com.littleinferno.flowchart.variable;

import android.os.Parcel;
import android.os.Parcelable;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.FlowchartProject;
import com.littleinferno.flowchart.project.ProjectModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AndroidVariableManager implements Parcelable, ProjectModule {

    private final String[] keyWords;

    private final FlowchartProject project;
    private final List<AndroidVariable> variables;

    private boolean error;
    private String errorStr;

    public AndroidVariableManager(FlowchartProject project) {
        this.project = project;

        keyWords = new String[]{
                "abstract", "arguments", "await", "boolean",
                "break", "byte", "case", "catch",
                "char", "class", "const", "continue",
                "debugger", "default", "delete", "do",
                "double", "else", "enum", "eval",
                "export", "extends", "false", "final",
                "finally", "float", "for", "function",
                "goto", "if", "implements", "import",
                "in", "instanceof", "int", "interface",
                "let", "long", "native", "new",
                "null", "package", "private", "protected",
                "public", "return", "short", "static",
                "super", "switch", "synchronized", "this",
                "throw", "throws", "transient", "true",
                "try", "typeof", "var", "void",
                "volatile", "while", "with", "yield"
        };
        variables = new ArrayList<>();
    }

    protected AndroidVariableManager(Parcel in) {
        keyWords = in.createStringArray();
        error = in.readByte() != 0;
        errorStr = in.readString();
        variables = new ArrayList<>();
        project = (FlowchartProject) in.readArrayList(FlowchartProject.class.getClassLoader()).get(0);

        in.readList(variables, AndroidVariable.class.getClassLoader());
    }

    public static final Creator<AndroidVariableManager> CREATOR = new Creator<AndroidVariableManager>() {
        @Override
        public AndroidVariableManager createFromParcel(Parcel in) {
            return new AndroidVariableManager(in);
        }

        @Override
        public AndroidVariableManager[] newArray(int size) {
            return new AndroidVariableManager[size];
        }
    };

    public AndroidVariable createVariable(final String name, final DataType dataType, final boolean isArray) {
        if (!checkVariableName(name))
            throw new RuntimeException(errorStr);

        variables.add(new AndroidVariable(this, dataType, name, isArray));
        return variables.get(variables.size() - 1);
    }

    public void removeVariable(AndroidVariable variable) {
        variables.remove(variable);
    }

    public void removeVariable(int position) {
        variables.remove(position);
    }

    public AndroidVariable getVariable(String name) {
        return Stream.of(variables)
                .filter(value -> value.getName().equals(name))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Cannot find varable with name:\"" + name + "\""));
    }

    public boolean checkVariableName(final String name) {

        if (name.isEmpty()) {
            errorStr = "Name can not be empty";
            error = true;
        } else if (!name.matches("[$a-zA-Z_][0-9a-zA-Z_$]*")) {
            errorStr = "Unacceptable symbols";
            error = true;
        } else if (Stream.of(variables).map(AndroidVariable::getName).anyMatch(name::equals)) {
            errorStr = "This name is already taken";
            error = true;
        } else if (Stream.of(keyWords).anyMatch(name::equals)) {
            errorStr = "Invalid name";
            error = true;
        } else {
            error = false;
            errorStr = "";
        }

        return !error;
    }

    public String getError() {
        return errorStr;
    }

    public List<AndroidVariable> getVariables() {
        return variables;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(variables);
        dest.writeStringArray(keyWords);
        dest.writeInt(error ? 0 : 1);
        dest.writeString(errorStr);
        dest.writeList(Collections.singletonList(getProject()));//TODO remove hack
    }

    @Override
    public FlowchartProject getProject() {
        return project;
    }
}
