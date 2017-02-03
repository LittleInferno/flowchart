package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.littleinferno.flowchart.DataType;

@Deprecated
public class Item extends Table {

    static class PropertyTable extends Table {

        interface TypeSelected {
            void select(DataType type);
        }

        interface TextEntered {
            void entered(String text);
        }

        PropertyTable(String text, Skin skin) {

            add(new Label("name", skin)).expandX().fillX().height(30);
            textField = new TextField(text, skin);
            add(textField).expandX().fillX().height(30).row();

            add(new Label("Data type", skin)).expandX().fillX().height(30);

            type = new SelectBox<DataType>(skin);
            type.setItems(DataType.BOOL,
                    DataType.FLOAT,
                    DataType.INT,
                    DataType.STRING);

            add(type).expandX().fillX().height(30).row();

            add(new Label("is array", skin)).expandX().fillX().height(30);

            array = new CheckBox("", skin);
            array.setChecked(false);
            add(array).expandX().fillX().height(30).row();
        }

        void addTypeSelectedListener(final TypeSelected typeSelected) {
            type.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    typeSelected.select(PropertyTable.this.type.getSelected());
                }
            });
        }

        void addTextEnteredListener(final TextEntered textEntered) {
            textField.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    textEntered.entered(PropertyTable.this.textField.getText());
                }
            });
        }

        private TextField textField;
        private SelectBox<DataType> type;
        public CheckBox array;

    }

}
