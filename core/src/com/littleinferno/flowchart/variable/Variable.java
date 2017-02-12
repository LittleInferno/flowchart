package com.littleinferno.flowchart.variable;

import com.annimon.stream.Stream;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.gui.SceneUi;
import com.littleinferno.flowchart.ui.Main;
import com.littleinferno.flowchart.util.ArrayChangedListener;
import com.littleinferno.flowchart.util.DataSelectBox;
import com.littleinferno.flowchart.util.DestroyListener;
import com.littleinferno.flowchart.util.InputForm;
import com.littleinferno.flowchart.util.NameChangedListener;
import com.littleinferno.flowchart.util.TypeChangedListener;

import java.util.ArrayList;
import java.util.List;

public class Variable {

    private DataType dataType;
    private String name;
    private boolean isArray;

    private List<NameChangedListener> nameChangedListeners;
    private List<TypeChangedListener> typeChangedListeners;
    private List<ArrayChangedListener> arrayChangedListeners;
    private List<DestroyListener> destroyListeners;

    private VariableDetailsTable variableDetailsTable;

    Variable(String name, DataType type, boolean isArray) {
        this.name = name;
        this.dataType = type;
        this.isArray = isArray;

        this.nameChangedListeners = new ArrayList<>();
        this.typeChangedListeners = new ArrayList<>();
        this.arrayChangedListeners = new ArrayList<>();
        this.destroyListeners = new ArrayList<>();

        this.variableDetailsTable = new VariableDetailsTable(this);
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType newType) {
        this.dataType = newType;

        notifyListenersTypeChanged(newType);
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;

        notifyListenersNameChanged(newName);
    }

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean isArray) {
        this.isArray = isArray;

        notifyListenersIsArrayChanged(isArray);
    }

    public String gen() {
        if (isArray)
            return String.format("var %s=[];\n", name);
        else
            return String.format("var %s;\n", name);
    }

    public void addListener(NameChangedListener listener) {
        nameChangedListeners.add(listener);
    }

    public void addListener(TypeChangedListener listener) {
        typeChangedListeners.add(listener);
    }

    public void addListener(ArrayChangedListener listener) {
        arrayChangedListeners.add(listener);
    }

    public void addListener(DestroyListener listener) {
        destroyListeners.add(listener);
    }

    private void notifyListenersNameChanged(String newName) {
        Stream.of(nameChangedListeners).forEach(var -> var.changed(newName));
    }

    private void notifyListenersTypeChanged(DataType newtype) {
        Stream.of(typeChangedListeners).forEach(var -> var.changed(newtype));
    }

    private void notifyListenersIsArrayChanged(boolean isArray) {
        Stream.of(arrayChangedListeners).forEach(var -> var.changed(isArray));
    }

    private void notifyListenersDestroed() {
        Stream.of(destroyListeners).forEach(DestroyListener::destroyed);
    }

    public VariableDetailsTable getTable() {
        return variableDetailsTable;
    }

    void destroy() {
        notifyListenersDestroed();
    }

    private static class VariableDetailsTable extends VisTable {

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
                    SceneUi.getVariableManager().removeVariable(variable);
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
            add(variableName).growX().expandY();
            add(deleteVariable).row();
            add(new VisLabel("type: "));
            add(variableType).growX().expandY();
            add(isArray);
        }

    }

}
