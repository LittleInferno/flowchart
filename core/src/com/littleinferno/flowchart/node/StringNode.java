package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.littleinferno.flowchart.codegen.Expression;
import com.littleinferno.flowchart.codegen.ExpressionGeneratable;
import com.littleinferno.flowchart.codegen.ValueExpression;
import com.littleinferno.flowchart.value.StringValue;
import com.littleinferno.flowchart.value.Value;

public class StringNode extends Node implements ExpressionGeneratable {

    private final TextField field;

    public StringNode(Skin skin) {
        super("String", true, skin);

        addDataOutputPin(Value.Type.STRING, "data");

        field = new TextField("", skin);
        left.add(field).expandX().fillX().minWidth(0);
    }

    @Override
    public void eval() throws Exception {
        getPin("data").setValue(new StringValue(field.getText()));
    }

    @Override
    public Expression genExpression() {
        return new ValueExpression(new StringValue(field.getText()));
    }
}
