package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.littleinferno.flowchart.codegen.Expression;
import com.littleinferno.flowchart.codegen.ExpressionGeneratable;
import com.littleinferno.flowchart.codegen.ValueExpression;
import com.littleinferno.flowchart.value.IntegerValue;
import com.littleinferno.flowchart.value.Value;

public class IntegerNode extends Node implements ExpressionGeneratable {

    private final TextField field;

    public IntegerNode(Skin skin) {
        super("Integer", true, skin);

        addDataOutputPin(Value.Type.INT, "data");

        field = new TextField("", skin);
        field.setTextFieldFilter(new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                if (c >= '0' && c <= '9' || c=='+' || c=='-')
                    return true;
                return false;
            }
        });
        left.add(field).expandX().fillX().minWidth(0);
    }

    @Override
    public void eval() throws Exception {
        getPin("data").setValue(new IntegerValue(Integer.valueOf(field.getText())));
    }

    @Override
    public Expression genExpression() {
        return new ValueExpression(new IntegerValue(Integer.valueOf(field.getText())));
    }
}
