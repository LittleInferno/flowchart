package com.littleinferno.flowchart.variable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.kotcrab.vis.ui.util.adapter.ArrayListAdapter;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisTable;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.project.Project;
import com.littleinferno.flowchart.util.BaseHandle;
import com.littleinferno.flowchart.util.managers.ProjectManager;

import java.util.ArrayList;
import java.util.List;

public class VariableManager extends ProjectManager {

    private ArrayList<Variable> variables;
    private int counter;
    private UI ui;

    public VariableManager(Project project) {
        super(project);

        variables = new ArrayList<>();
        counter = 0;
        ui = new UI(variables);
    }

    public VariableManager(Project project, VariableManagerHandle variableManagerHandle) {
        this(project);

        counter = variableManagerHandle.counter;

        Stream.of(variableManagerHandle.variableHandles)
                .forEach(this::createVariable);
    }

    private Variable createVariable(Variable.VariableHandle variableHandle) {
        Variable variable = new Variable(variableHandle);

        variables.add(variable);
        ui.update();

        return variable;
    }

    public Variable createVariable() {
        Variable variable = new Variable("newVar" + counter++, DataType.BOOL, false);

        variables.add(variable);
        ui.update();

        return variable;
    }

    void removeVariable(Variable variable) {
        variable.destroy();
        variables.remove(variable);
        ui.update();
    }

    public Variable getVariable(String name) {
        return Stream.of(variables)
                .filter(value -> value.getName().equals(name))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Cannot find varable with title:\"" + name + "\""));
    }

    public String gen(BaseCodeGenerator builder) {
        return Stream.of(variables)
                .map(variable -> variable.gen(builder))
                .collect(Collectors.joining());
    }

    public UI getUi() {
        return ui;
    }

    public VariableManagerHandle getHandle() {
        return new VariableManagerHandle(counter,
                Stream.of(variables).map(Variable::getHandle).toList());
    }

    @Override
    public void dispose() {
        Stream.of(variables).forEach(Variable::destroy);
        variables.clear();
    }

    @SuppressWarnings("WeakerAccess")
    public static class UI {

        private ArrayListAdapter<Variable, VisTable> variableAdapter;
        private final VisTable detailsTable;
        private final VisTable varTable;

        public VisTable getVarTable() {
            return varTable;
        }

        public VisTable getDetailsTable() {
            return detailsTable;
        }

        void update() {
            variableAdapter.itemsChanged();
        }

        UI(ArrayList<Variable> variables) {
            variableAdapter = new VariableListAdapter(variables);

            detailsTable = new VisTable(true);
            varTable = new VisTable(true);

            ListView<Variable> view = new ListView<>(variableAdapter);

            view.setItemClickListener(item -> {
                detailsTable.clearChildren();
                detailsTable.add(item.getTable()).grow();
            });

            varTable.add(view.getMainTable()).grow().row();
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static class VariableManagerHandle implements BaseHandle {
        int counter;
        List<Variable.VariableHandle> variableHandles;

        @SuppressWarnings("unused")
        public VariableManagerHandle() {
        }

        public VariableManagerHandle(int counter, List<Variable.VariableHandle> variableHandles) {
            this.counter = counter;
            this.variableHandles = variableHandles;
        }
    }
}
