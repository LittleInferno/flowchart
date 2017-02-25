package com.littleinferno.flowchart.variable;

import com.annimon.stream.Stream;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.kotcrab.vis.ui.util.adapter.ArrayListAdapter;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisTable;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;

import java.util.ArrayList;

public class VariableManager implements Json.Serializable {

    private ArrayList<Variable> variables;
    private int counter;
    private UI ui;

    public VariableManager() {
        variables = new ArrayList<>();
        counter = 0;
        ui = new UI(variables);
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

    public String gen(BaseCodeGenerator builder) {
        final StringBuilder stringBuilder = new StringBuilder();

        Stream.of(variables)
                .forEach(variable -> stringBuilder.append(variable.gen(builder)));

        return stringBuilder.toString();
    }

    @Override
    public void write(Json json) {
        json.writeValue("counter", counter);
        json.writeArrayStart("variables");

        for (Variable v : variables)
        {
            json.writeObjectStart();
            json.writeField(v, "name", "name");
            json.writeValue("dataType", v.getDataType());
            json.writeField(v, "isArray", "isArray");
            json.writeObjectEnd();
        }

        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {

        counter = jsonData.get("counter").asInt();

        JsonValue vars = jsonData.get("variables");
        for (JsonValue element : vars) {
            variables.add(json.readValue(Variable.class, element));
        }
    }

    public UI getUi() {
        return ui;
    }

    public static class UI {

        private ArrayListAdapter<Variable, VisTable> variableAdapter;

        private final VisTable detailsTable;

        public VisTable getVarTable() {
            return varTable;
        }

        public VisTable getDetailsTable() {
            return detailsTable;
        }

        public void update() {
            variableAdapter.itemsChanged();
        }

        private final VisTable varTable;

        public UI(ArrayList<Variable> variables) {
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
}
