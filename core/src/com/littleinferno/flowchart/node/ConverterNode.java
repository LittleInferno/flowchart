package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.codegen.ConversionExpression;
import com.littleinferno.flowchart.codegen.Expression;
import com.littleinferno.flowchart.codegen.ExpressionGeneratable;
import com.littleinferno.flowchart.value.Value;

public class ConverterNode extends Node implements ExpressionGeneratable {
    public ConverterNode(Value.Type from, Value.Type to, Skin skin) {
        super("Converter", true, skin);

        addDataInputPin(from, "from");
        addDataOutputPin(to, "to");
    }

    @Override
    public void eval() throws Exception {
        getPin("to").setValue(genExpression().eval());
    }

    @Override
    public Expression genExpression() {
        ExpressionGeneratable node = (ExpressionGeneratable) getPin("from").getConnectionNode();

        return new ConversionExpression(node.genExpression());
    }


}
