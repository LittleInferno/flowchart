package com.littleinferno.flowchart.node;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.littleinferno.flowchart.function.Function;
import com.littleinferno.flowchart.project.Project;

import java.util.UUID;

public abstract class FunctionNode extends Node {

    protected Function function;

    FunctionNode(Function function, String name, boolean closable) {
        super(name, closable);
        this.function = function;
    }

    public Function getFunction() {
        return function;
    }

    static public class FunctionNodeSerializer<T extends FunctionNode> implements Json.Serializer<T> {
        @Override
        public void write(Json json, T object, Class knownType) {
            json.writeObjectStart();
            json.writeValue("x", object.getX());
            json.writeValue("y", object.getY());
            json.writeValue("function", object.getFunction().getName());
            json.writeValue("id", object.getId().toString());
            json.writeObjectEnd();
        }

        @Override
        public T read(Json json, JsonValue jsonData, Class type) {

            float x = jsonData.get("x").asFloat();
            float y = jsonData.get("y").asFloat();
            UUID id = UUID.fromString(jsonData.get("id").asString());
            String funName = jsonData.get("function").asString();
            Function function = Project.instance().getFunctionManager().getFunction(funName);

            T node = NodeManager.create(type, function);

            node.setPosition(x, y);
            node.setId(id);

            return node;
        }
    }
}
