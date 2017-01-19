package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import com.littleinferno.flowchart.Function;
import com.littleinferno.flowchart.codegen.CodeGen;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.value.Value;

public class FunctionReturnNode extends Node implements CodeGen {
    private Function function;

    public FunctionReturnNode(Function function, Skin skin) {
        super(function.getName(), false, skin);
        this.function = function;

        addExecutionInputPin();
    }

    @Override
    public String gen() {

        Array<Pin> input = getInput();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < input.size; i++) {
            if (input.get(i).getType() != Value.Type.EXECUTION)
                builder.append("var ").
                        append(input.get(i).getName()).
                        append(" = ");

            CodeGen node = (CodeGen) input.get(i).getConnectionNode();

            builder.append(node.gen()).append("\n");
        }


        return null;
    }

//    @Override
//    public Statement genStatement() {
//
//        Array<Pin> inputs = getInput();
//
//        ArrayList<Expression> returns = new ArrayList<Expression>(inputs.size - 1);
//        for (Pin i : inputs) {
//            if (i.getType() != Value.Type.EXECUTION) {
//                Pin pin = i.getConnectionPin();
//                returns.add(pin.genExpression());
//            }
//        }
//
//        return new FunctionalReturnStatement(returns);
//    }
}
