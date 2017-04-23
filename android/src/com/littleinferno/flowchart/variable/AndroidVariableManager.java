package com.littleinferno.flowchart.variable;

import android.os.Parcel;
import android.os.Parcelable;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.project.FlowchartProject;
import com.littleinferno.flowchart.project.ProjectModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AndroidVariableManager implements Parcelable, ProjectModule {

    public static final String TAG = "VARIABLE_MANAGER";

    private final FlowchartProject project;
    private final List<AndroidVariable> variables;

    public AndroidVariableManager(FlowchartProject project) {
        this.project = project;

        variables = new ArrayList<>();
    }

    protected AndroidVariableManager(Parcel in) {
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
        String s = checkVariableName(name);
        if (s != null)
            throw new RuntimeException(s);

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

    public String checkVariableName(final String name) {

        if (name.isEmpty()) {
            return "Name can not be empty";
        } else if (!name.matches("[$a-zA-Z_][0-9a-zA-Z_$]*")) {
            return "Unacceptable symbols";
        } else if (Stream.of(variables).map(AndroidVariable::getName).anyMatch(name::equals)) {
            return "This name is already taken";
        } else if (project.getPluginManager().getCodeGenerator().containsWord(name))
            return "Invalid name";

        return null;
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
        dest.writeList(Collections.singletonList(getProject()));//TODO remove hack
    }

    @Override
    public FlowchartProject getProject() {
        return project;
    }
}
