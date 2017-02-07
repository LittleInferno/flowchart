package com.littleinferno.flowchart.variable;

import com.annimon.stream.Stream;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.form.FormInputValidator;
import com.kotcrab.vis.ui.util.form.SimpleFormValidator;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.Destroyable;
import com.littleinferno.flowchart.VariableChangedAdaptor;
import com.littleinferno.flowchart.VariableChangedListener;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.ui.Main;

import java.util.ArrayList;
import java.util.List;

public class Variable implements Destroyable {

    private DataType dataType;
    private String name;
    private List<Node> nodes;
    private boolean isArray;
    private List<VariableChangedListener> changedListeners;

    private VariableDetailsTable variableDetailsTable;

    public Variable(String name, DataType type, boolean isArray) {
        this.name = name;
        this.dataType = type;
        this.isArray = isArray;
        this.nodes = new ArrayList<>();
        this.changedListeners = new ArrayList<>();
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

    public void addNode(Node node) {
        nodes.add(node);
    }

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean isArray) {
        this.isArray = isArray;

        notifyListenersIsArrayChanged(isArray);
    }

    public void removeNode(Node node) {
        nodes.remove(node);
    }

    public String gen() {
        if (isArray)
            return String.format("var %s=[];\n", name);
        else
            return String.format("var %s;\n", name);
    }

    public void addListener(VariableChangedListener listener) {
        changedListeners.add(listener);
    }

    private void notifyListenersNameChanged(String newName) {
        Stream.of(changedListeners).forEach(var -> var.nameChanged(newName));
    }

    private void notifyListenersTypeChanged(DataType newtype) {
        Stream.of(changedListeners).forEach(var -> var.typeChanged(newtype));
    }

    private void notifyListenersIsArrayChanged(boolean isArray) {
        Stream.of(changedListeners).forEach(var -> var.isArrayChanged(isArray));
    }

    private void notifyListenersDestroed() {
        Stream.of(changedListeners).forEach(VariableChangedListener::destroed);
    }

    public VariableDetailsTable getTable() {
        return variableDetailsTable;
    }

    @Override
    public void destroy() {
        notifyListenersDestroed();
    }

    private static class VariableInputForm extends VisWindow {

        private Variable variable;
        private VisValidatableTextField nameField;

        VariableInputForm(Variable variable) {
            super("variable name");
            this.variable = variable;

            initForm();
        }

        private void initForm() {
            TableUtils.setSpacingDefaults(this);
            defaults().padRight(1);
            defaults().padLeft(1);
            columnDefaults(0).left();

            VisTextButton cancelButton = new VisTextButton("cancel");
            VisTextButton acceptButton = new VisTextButton("accept");

            nameField = new VisValidatableTextField(variable.getName());
            VisLabel errorLabel = new VisLabel();
            errorLabel.setColor(Color.RED);

            VisTable buttonTable = new VisTable(true);
            buttonTable.add(errorLabel).grow();
            buttonTable.add(cancelButton);
            buttonTable.add(acceptButton);

            add(new VisLabel("variable name: "));
            add(nameField).grow().row();
            add(buttonTable).grow().colspan(2).padBottom(3);

            pack();

            SimpleFormValidator validator;
            validator = new SimpleFormValidator(acceptButton, errorLabel, "smooth");
            validator.setSuccessMessage("ok");
            validator.notEmpty(nameField, "cannot be empty");
            validator.custom(nameField, new FormInputValidator("Characters: _A-Za-z0-9") {
                @Override
                protected boolean validate(String input) {
                    return input.matches("([_A-Za-z][_A-Za-z0-9]*)");
                }
            });

            acceptButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    variable.setName(nameField.getText());
                    close();
                }
            });

            cancelButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    close();
                }
            });
        }

        void focus() {
            nameField.focusField();
            nameField.setCursorAtTextEnd();
            getStage().setKeyboardFocus(nameField);
        }
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
                    VariableInputForm inputForm = new VariableInputForm(variable);
                    inputForm.setPosition(w - inputForm.getWidth() / 2, h);
                    getStage().addActor(inputForm);
                    inputForm.focus();
                }
            });

            variable.addListener(new VariableChangedAdaptor() {
                @Override
                public void nameChanged(String newName) {
                    variableName.setText(newName);
                }
            });

            final VisImageButton deleteVariable = new VisImageButton("close");

            deleteVariable.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    VariableManager.instance.removeVariable(variable);
                }
            });

            final VisSelectBox<DataType> variableType = new VisSelectBox<>();

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
            add(variableName).growX().expandY();
            add(deleteVariable).row();
            add(new VisLabel("type: "));
            add(variableType).growX().expandY();
            add(isArray);
        }

    }

}
