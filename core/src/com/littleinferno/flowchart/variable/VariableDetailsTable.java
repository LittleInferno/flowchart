package com.littleinferno.flowchart.variable;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.littleinferno.flowchart.project.Project;
import com.littleinferno.flowchart.util.DataSelectBox;
import com.littleinferno.flowchart.util.InputForm;

class VariableDetailsTable extends VisTable {

    private Variable variable;

    VariableDetailsTable(Variable variable) {
        super(true);
        this.variable = variable;

        initTable();
    }

    private void initTable() {
        VisTextButton variableName = new VisTextButton(variable.getName());
        variableName.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                float w = getStage().getWidth() / 2;
                float h = getStage().getHeight() / 2;
                InputForm inputForm =
                        new InputForm("variable name", variable::getName, variable::setName);
                inputForm.setPosition(w - inputForm.getWidth() / 2, h);
                getStage().addActor(inputForm);
                inputForm.focus();
            }
        });

        variable.addListener(variableName::setText);

        final VisImageButton deleteVariable = new VisImageButton("close");

        deleteVariable.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Project.instance().getVariableManager().removeVariable(variable);
            }
        });

        final DataSelectBox variableType = new DataSelectBox();

        variableType.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                variable.setDataType(variableType.getSelected());
            }
        });

        VisImageButton.VisImageButtonStyle style =
                VisUI.getSkin().get("toggle", VisImageButton.VisImageButtonStyle.class);

        Drawable drawable = VisUI.getSkin().getDrawable("array");

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
        add(variableName).growX().expandY();
        add(deleteVariable).row();
        add(new VisLabel("type: "));
        add(variableType).growX().expandY();
        add(isArray);
    }

}
