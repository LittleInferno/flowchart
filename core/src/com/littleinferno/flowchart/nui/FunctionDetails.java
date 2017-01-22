package com.littleinferno.flowchart.nui;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.CollapsibleWidget;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.littleinferno.flowchart.Function;
import com.littleinferno.flowchart.parameter.Parameter;
import com.littleinferno.flowchart.value.Value;

public class FunctionDetails extends VisTable {

    Function function;

    int counter = 0;

    FunctionDetails(Function function) {
        this.function = function;
        init();
    }

    private void init() {
       // setFillParent(true);

        VisTextField functionName = new VisTextField();

        add(new VisLabel("name: "));
        add(functionName).fillX().expandX().colspan(2).row();


        VisTextButton collapseInput = new VisTextButton(">");
        add(collapseInput);
        add(new VisLabel("inputs")).fillX().expandX();
        VisTextButton addInput = new VisTextButton("add");
        add(addInput).row();

        final VisTable input = new VisTable();
        final CollapsibleWidget inputCollapsible = new CollapsibleWidget(input);

        add(inputCollapsible).expandX().fillX().colspan(3).row();

        addInput.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addInputParameter(input);
            }
        });

        collapseInput.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                inputCollapsible.setCollapsed(!inputCollapsible.isCollapsed());
            }
        });

        VisTextButton collapseOutput = new VisTextButton(">");
        add(collapseOutput);
        add(new VisLabel("outputs")).fillX().expandX();
        VisTextButton addOutput = new VisTextButton("add");
        add(addOutput).row();
        final VisTable output = new VisTable();
        final CollapsibleWidget outputCollapse = new CollapsibleWidget(output);

        add(outputCollapse).expandX().fillX().colspan(3).row();

        addOutput.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addOutputParameter(output);
            }
        });

        collapseOutput.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                outputCollapse.setCollapsed(!outputCollapse.isCollapsed());
            }
        });

    }


    private void addInputParameter(VisTable table) {

        //  final Parameter parameter = new InputParameter(function, "newInParam" + counter++, Value.Type.BOOL);

        VisTable paramTable = new VisTable();

        VisTextField parameterName = new VisTextField("newParam" + counter++);
        VisSelectBox<Value.Type> parameterType = new VisSelectBox<Value.Type>();

        parameterType.setItems(Value.Type.BOOL,
                Value.Type.FLOAT,
                Value.Type.INT,
                Value.Type.STRING);

        paramTable.add(parameterName).fill().expand();
        paramTable.addSeparator(true);
        paramTable.add(parameterType).fill().expand();

        table.add(paramTable).row();
    }

    private void addOutputParameter(VisTable table) {

        //  final Parameter parameter = new InputParameter(function, "newInParam" + counter++, Value.Type.BOOL);

        VisTable paramTable = new VisTable();

        VisTextField parameterName = new VisTextField("newParam" + counter++);
        VisSelectBox<Value.Type> parameterType = new VisSelectBox<Value.Type>();

        parameterType.setItems(Value.Type.BOOL,
                Value.Type.FLOAT,
                Value.Type.INT,
                Value.Type.STRING);

        VisTextButton isArray = new VisTextButton("arr", "toggle");
        VisTextButton delete = new VisTextButton("del");


        paramTable.add(parameterName);
        paramTable.add(parameterType).padLeft(5);
        paramTable.add(isArray);
        paramTable.add(delete).padLeft(5);

        table.add(paramTable).row();
    }

    private void addParameter(Parameter parameter) {
        VisTable paramTable = new VisTable();

        VisTextField parameterName = new VisTextField("newParam" + counter++);
        VisSelectBox<Value.Type> parameterType = new VisSelectBox<Value.Type>();

        parameterType.setItems(Value.Type.BOOL,
                Value.Type.FLOAT,
                Value.Type.INT,
                Value.Type.STRING);

        VisTextButton isArray = new VisTextButton("arr", "toggle");
        VisTextButton delete = new VisTextButton("del");


        paramTable.add(parameterName);
        paramTable.add(parameterType).padLeft(5);
        paramTable.add(isArray);
        paramTable.add(delete).padLeft(5);


    }


}
