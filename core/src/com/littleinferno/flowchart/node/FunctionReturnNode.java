package com.littleinferno.flowchart.node;

import com.badlogic.gdx.utils.Array;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.Function;
import com.littleinferno.flowchart.NameChangeable;
import com.littleinferno.flowchart.VariableChangedListener;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.parameter.Parameter;
import com.littleinferno.flowchart.parameter.ParameterListener;
import com.littleinferno.flowchart.pin.Pin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionReturnNode extends Node {

    Function function;

    public FunctionReturnNode(Function function) {
        super(function.getName(), false);
        this.function = function;
        addExecutionInputPin();

        function.addListener(new NameChangeable.NameChange() {
            @Override
            public void changed(String newName) {
                setTitle(newName);
            }
        });

        function.addListener(new ParameterListener() {
            private List<Pin> pins = new ArrayList<Pin>();

            @Override
            public void added(Parameter parameter) {
                if (parameter.getConnection() == Connection.OUTPUT) {
                    final Pin pin = addDataInputPin(parameter.getDataType(), parameter.getName());

                    pin.setArray(parameter.isArray());

                    parameter.addListener(new VariableChangedListener() {
                        @Override
                        public void nameChanged(String newName) {
                            pin.setName(newName);
                        }

                        @Override
                        public void typeChanged(DataType newType) {
                            pin.setType(newType);
                        }

                        @Override
                        public void isArrayChanged(boolean isArray) {
                            pin.setArray(isArray);
                        }

                        @Override
                        public void destroed() {
                            close();
                        }
                    });

                    pins.add(pin);
                }
            }

            @Override
            public void removed(Parameter parameter) {
                for (Pin pin : pins)
                    if (parameter.getName().equals(pin.getName()))
                        removePin(pin);
            }
        });
        function.addReturnNode(this);

    }

    @Override
    public String gen(CodeBuilder builder, Pin with) {
        Array<Pin> input = getInput();

        Map<String, String> returnPack = new HashMap<String, String>();

        for (Pin i : input) {

            if (i.getType() != DataType.EXECUTION) {
                Pin.Connector node = i.getConnector();

                String p = node.parent.gen(builder, node.pin);
                returnPack.put(i.getName(), p);
            }
        }

        return builder.createReturn(returnPack);
    }

    @Override
    public void close() {
        super.close();
        function.removeNode(this);
    }
}
