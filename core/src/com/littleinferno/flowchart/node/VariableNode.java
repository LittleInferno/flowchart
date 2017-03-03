package com.littleinferno.flowchart.node;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.littleinferno.flowchart.project.Project;
import com.littleinferno.flowchart.variable.Variable;

import java.util.UUID;

public abstract class VariableNode extends Node {

    protected final Variable variable;

    VariableNode(Variable variable, String name) {
        super(name, true);
        this.variable = variable;
    }

    public Variable getVariable() {
        return variable;
    }

    static public class VariableNodeSerializer<T extends VariableNode> implements Json.Serializer<T> {
        @Override
        public void write(Json json, T object, Class knownType) {
            json.writeObjectStart();
            json.writeValue("x", object.getX());
            json.writeValue("y", object.getY());
            json.writeValue("variable", object.getVariable().getName());
            json.writeValue("id", object.getId().toString());
            json.writeObjectEnd();
        }

        @Override
        public T read(Json json, JsonValue jsonData, Class type) {

            float x = jsonData.get("x").asFloat();
            float y = jsonData.get("y").asFloat();
            UUID id = UUID.fromString(jsonData.get("id").asString());
            String varName = jsonData.get("variable").asString();
            Variable variable = Project.instance().getVariableManager().getVariable(varName);

            T node = NodeManager.create(type, variable);

            node.setPosition(x, y);
            node.setId(id);

            return node;
        }
    }
}
