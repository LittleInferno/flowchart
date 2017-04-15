package com.littleinferno.flowchart.variable;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.FlowchartProject;

import java.util.ArrayList;
import java.util.List;

public class AndroidVariableManager {

    private final String[] keyWords;
    private final List<AndroidVariable> variables;
    private final FlowchartProject project;
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

    public AndroidVariable createVariable(final DataType dataType, final String name, final boolean isArray) {
        variables.add(new AndroidVariable(dataType, name, isArray));
        return variables.get(variables.size() - 1);
    }

    void removeVariable(AndroidVariable variable) {
        variables.remove(variable);
    }

    public AndroidVariable getVariable(String name) {
        return Stream.of(variables)
                .filter(value -> value.getName().equals(name))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Cannot find varable with title:\"" + name + "\""));
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
}
