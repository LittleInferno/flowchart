package com.littleinferno.flowchart.tempgui.variable;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.Variable;
import com.littleinferno.flowchart.ui.Main;

class VariableDetails extends VisTable {

    Variable variable;
    private VariableTable table;

    VariableDetails(VariableTable table, Variable variable) {
        super(true);
        this.table = table;
        this.variable = variable;
        init();
    }

    private void init() {
        top();

        final VisValidatableTextField variableName = new VisValidatableTextField(variable.getName());

        variableName.addValidator(new InputValidator() {
            @Override
            public boolean validateInput(String input) {
                return input.matches("([_A-Za-z][_A-Za-z0-9]*)");
            }
        });

        variableName.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (variableName.isInputValid()) {
                    variable.setName(variableName.getText());
                }
            }
        });

        final VisImageButton deleteFunction = new VisImageButton("close");

        deleteFunction.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //  variable.delete();
                // table.deleteFunction(FunctionDetails.this);
            }
        });

        final VisSelectBox<DataType> variableType = new VisSelectBox<DataType>();

        variableType.setItems(DataType.BOOL,
                DataType.FLOAT,
                DataType.INT,
                DataType.STRING);

        variableType.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                variable.setDataType(variableType.getSelected());
            }
        });

        VisImageButton.VisImageButtonStyle style =
                VisUI.getSkin().get("toggle", VisImageButton.VisImageButtonStyle.class);

        Drawable drawable = (Main.scale == VisUI.SkinScale.X1) ?
                Main.skin.getDrawable("array-X1") :
                Main.skin.getDrawable("array-X2");

        style.imageChecked = drawable;
        style.imageUp = drawable;

        final VisImageButton isArray = new VisImageButton(style);
        isArray.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                variable.setArray(isArray.isChecked());
            }
        });

        add(new VisLabel("name: "));
        add(variableName).growX().expandY().colspan(2).row();
        add(new VisLabel("type: "));
        add(variableType).growX().expandY();
        add(isArray);
    }

    @Override
    public String getName() {
        return variable.getName();
    }
}
