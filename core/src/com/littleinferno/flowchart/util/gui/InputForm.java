package com.littleinferno.flowchart.util.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.form.FormInputValidator;
import com.kotcrab.vis.ui.util.form.SimpleFormValidator;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import com.kotcrab.vis.ui.widget.VisWindow;


public class InputForm extends VisWindow {
    private GetName getName;
    private SetName setName;

    private VisValidatableTextField nameField;

    public InputForm(String title, GetName getName, SetName setName) {
        super(title);

        this.getName = getName;
        this.setName = setName;
        this.nameField = new VisValidatableTextField(getName.get());

        init();
    }

    private void init() {
        TableUtils.setSpacingDefaults(this);
        defaults().padRight(1);
        defaults().padLeft(1);
        columnDefaults(0).left();

        VisTextButton cancelButton = new VisTextButton("cancel");
        VisTextButton acceptButton = new VisTextButton("accept");

        VisLabel errorLabel = new VisLabel();
        errorLabel.setColor(Color.RED);

        VisTable buttonTable = new VisTable(true);
        buttonTable.add(errorLabel).grow();
        buttonTable.add(cancelButton);
        buttonTable.add(acceptButton);

        add(new VisLabel("title: "));
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
                setName.set(nameField.getText());
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

    public void focus() {
        nameField.focusField();
        nameField.setCursorAtTextEnd();
        getStage().setKeyboardFocus(nameField);
    }

    public interface GetName {
        String get();
    }

    public interface SetName {
        void set(final String string);
    }
}
