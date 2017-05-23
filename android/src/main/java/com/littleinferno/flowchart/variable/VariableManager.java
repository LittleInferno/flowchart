package com.littleinferno.flowchart.variable;

import android.os.Parcel;
import android.os.Parcelable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.littleinferno.flowchart.project.Project;
import com.littleinferno.flowchart.project.ProjectModule;
import com.littleinferno.flowchart.util.DataType;
import com.littleinferno.flowchart.util.Fun;
import com.littleinferno.flowchart.util.Generator;
import com.littleinferno.flowchart.util.Link;

import java.util.ArrayList;
import java.util.List;

public class VariableManager extends ProjectModule implements Parcelable, Generator {

    public static final String TAG = "VARIABLE_MANAGER";

    private final List<Variable> variables;
    private final List<Link> variableAddListeners = new ArrayList<>();
    private final List<Link> variableRemoveListeners = new ArrayList<>();

    public VariableManager(Project project) {
        super(project);

        variables = new ArrayList<>();
    }

    private VariableManager(Parcel in) {
        super(in);
        variables = new ArrayList<>();
        in.readList(variables, Variable.class.getClassLoader());
    }

    public static final Creator<VariableManager> CREATOR = new Creator<VariableManager>() {
        @Override
        public VariableManager createFromParcel(Parcel in) {
            return new VariableManager(in);
        }

        @Override
        public VariableManager[] newArray(int size) {
            return new VariableManager[size];
        }
    };

    public Variable createVariable(final String name, final DataType dataType, final boolean isArray) {
        String s = checkVariableName(name);
        if (s != null)
            throw new RuntimeException(s);

        variables.add(new Variable(this, dataType, name, isArray));
        notifyVariableAdd();
        return variables.get(variables.size() - 1);
    }

    public void removeVariable(Variable variable) {
        variables.remove(variable);
    }

    public void removeVariable(int position) {
        variables.remove(position);
        notifyVariableRemove();
    }

    public Variable getVariable(String name) {
        return Stream.of(variables)
                .filter(value -> value.getName().equals(name))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Cannot find varable with name:\"" + name + "\""));
    }

    public Variable getVariable(int pos) {
        return variables.get(pos);
    }

    public String checkVariableName(final String name) {

        if (name.isEmpty()) {
            return "Name can not be empty";
        } else if (!getProject().getRules().checkPattern(name)) {
            return "Unacceptable symbols";
        } else if (Stream.of(variables).map(Variable::getName).anyMatch(name::equals)) {
            return "This name is already taken";
        } else if (getProject().getRules().containsWord(name))
            return "Invalid name";

        return null;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeList(variables);
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

    public Variable createVariable(Variable.SimpleObject s) {
        return createVariable(s.name, s.dataType, s.isArray);
    }

    @Override
    public String generate() {
        return Stream.of(variables).map(Variable::generate).collect(Collectors.joining("\n"));
    }
}
