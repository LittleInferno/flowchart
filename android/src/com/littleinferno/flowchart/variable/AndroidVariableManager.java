package com.littleinferno.flowchart.variable;

import android.os.Parcel;
import android.os.Parcelable;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.project.FlowchartProject;
import com.littleinferno.flowchart.project.ProjectModule;
import com.littleinferno.flowchart.util.Fun;
import com.littleinferno.flowchart.util.Link;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AndroidVariableManager implements Parcelable, ProjectModule {

    public static final String TAG = "VARIABLE_MANAGER";

    private final FlowchartProject project;
    private final List<AndroidVariable> variables;
    private final List<Link> variableAddListeners;
    private final List<Link> variableRemoveListeners;

    public AndroidVariableManager(FlowchartProject project) {
        this.project = project;

        variables = new ArrayList<>();
        variableAddListeners = new ArrayList<>();
        variableRemoveListeners = new ArrayList<>();
    }

    private AndroidVariableManager(Parcel in) {
        variables = new ArrayList<>();
        project = (FlowchartProject) in.readArrayList(FlowchartProject.class.getClassLoader()).get(0);

        in.readList(variables, AndroidVariable.class.getClassLoader());

        variableAddListeners = new ArrayList<>();
        in.readList(variableAddListeners, Fun.class.getClassLoader());
        variableRemoveListeners = new ArrayList<>();
        in.readList(variableRemoveListeners, Fun.class.getClassLoader());
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
        notifyVariableAdd();
        return variables.get(variables.size() - 1);
    }

    public void removeVariable(AndroidVariable variable) {
        variables.remove(variable);
    }

    public void removeVariable(int position) {
        variables.remove(position);
        notifyVariableRemove();
    }

    public AndroidVariable getVariable(String name) {
        return Stream.of(variables)
                .filter(value -> value.getName().equals(name))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Cannot find varable with name:\"" + name + "\""));
    }

    public AndroidVariable getVariable(int pos) {
        return variables.get(pos);
    }

    public String checkVariableName(final String name) {

        if (name.isEmpty()) {
            return "Name can not be empty";
        } else if (!project.getPluginManager().getRules().checkPattern(name)) {
            return "Unacceptable symbols";
        } else if (Stream.of(variables).map(AndroidVariable::getName).anyMatch(name::equals)) {
            return "This name is already taken";
        } else if (project.getPluginManager().getRules().containsWord(name))
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
        dest.writeList(Collections.singletonList(getProject()));//TODO action hack
        dest.writeList(variableAddListeners);
        dest.writeList(variableRemoveListeners);
    }

    @Override
    public FlowchartProject getProject() {
        return project;
    }

    public Link onVariableAdd(Fun fun) {
        Link link = new Link(variableAddListeners, fun);
        variableAddListeners.add(link);
        return link;
    }

    public Link onVariableRemove(Fun fun) {
        Link link = new Link(variableAddListeners, fun);
        variableRemoveListeners.add(link);
        return link;
    }

    private void notifyVariableAdd() {
        Stream.of(variableAddListeners).forEach(Link::call);
    }

    private void notifyVariableRemove() {
        Stream.of(variableRemoveListeners).forEach(Link::call);
    }

    public void updateData() {
        notifyVariableAdd();
    }
}
