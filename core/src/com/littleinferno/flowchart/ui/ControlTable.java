package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.littleinferno.flowchart.Variable;
import com.littleinferno.flowchart.node.VariableGetNode;
import com.littleinferno.flowchart.node.VariableSetNode;
import com.littleinferno.flowchart.value.Value;


public class ControlTable extends Table {

    private class VariableItem extends Table {
        VariableItem(String varName, final Skin skin) {

            variable = new Variable(varName);
            variable.setValueType(Value.Type.BOOL);


            Table title = new Table();

            final Label name = new Label(varName, skin);
            name.setEllipsis(true);
            title.add(name).expand().fill()
                    .width(151);// TODO WTF? Fix it

            final Button set = new TextButton("set", skin);
            title.add(set).size(30).right();

            final Button get = new TextButton("get", skin);
            title.add(get).size(30).right();

            Table property = new Table();

            property.add(new Label("Var name", skin)).expandX().fillX().height(30);
            final TextField nameField = new TextField("", skin);
            property.add(nameField).expandX().fillX().height(30).row();

            property.add(new Label("Data type", skin)).expandX().fillX().height(30);

            final SelectBox<Value.Type> type = new SelectBox<Value.Type>(skin);
            type.setItems(Value.Type.BOOL,
                    Value.Type.FLOAT,
                    Value.Type.INT,
                    Value.Type.STRING);

            property.add(type).expandX().fillX().height(30);

            Tree tree = new Tree(skin);

            Tree.Node titleNode = new Tree.Node(title);
            titleNode.setSelectable(false);
            Tree.Node propertyNode = new Tree.Node(property);
            propertyNode.setSelectable(false);
            titleNode.add(propertyNode);

            tree.add(titleNode);
            add(tree).fillX().expandX();
            tree.layout();
            property.pack();
            Gdx.app.log(tree.getWidth()+"",property.getWidth()+"");

            nameField.setTextFieldListener(new TextField.TextFieldListener() {
                @Override
                public void keyTyped(TextField textField, char c) {
                    variable.setName(nameField.getText());
                    name.setText(nameField.getText());
                }
            });

            type.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    variable.setValueType(type.getSelected());
                }
            });

            Main.getDND().addSource(new DragAndDrop.Source(get) {
                @Override
                public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                    DragAndDrop.Payload payload = new DragAndDrop.Payload();
                    payload.setObject(new VariableGetNode(new Vector2(200, 200), variable));

                    payload.setDragActor(new Label(String.format("Get %s", variable.getName()), skin));

                    return payload;
                }
            });

            Main.getDND().addSource(new DragAndDrop.Source(set) {
                @Override
                public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                    DragAndDrop.Payload payload = new DragAndDrop.Payload();
                    payload.setObject(new VariableSetNode(new Vector2(200, 300), variable));

                    payload.setDragActor(new Label(String.format("Set %s", variable.getName()), skin));

                    return payload;
                }
            });

        }

        Variable variable;
    }

//    private static class FunctionItem extends Table {
//
//        FunctionItem(String funName, Skin skin) {
//
//            Table title = new Table();
//
//            final Label name = new Label(funName, skin);
//            title.add(name).width(100).height(30);
//
//            Button set = new TextButton("ed", skin);
//            title.add(set).size(30).left();
//
//            Button get = new TextButton("use", skin);
//            title.add(get).size(30).left();
//
//
//            Table property = new Table();
//            property.setSize(160, 200);
//
//            Tree inputTree = new Tree(skin);
//
//            Tree.Node inputNode = new Tree.Node(new Label("input", skin));
//            inputNode.setSelectable(false);
//
//            VerticalGroup inputList = new VerticalGroup();
//            ScrollPane inputScroll = new ScrollPane(inputList);
//            Table container = new Table();
//            container.add(inputScroll).expandX().fillX().height(100);
//            container.setHeight(100);
//
//            Tree.Node inputContainerNode = new Tree.Node(container);
//            inputContainerNode.setSelectable(false);
//
//            inputNode.add(inputContainerNode);
//
//            inputTree.add(inputNode);
//        }
//    }

    private static class ComponentsTable extends Table {
        ComponentsTable(Skin skin) {

            List<String> list = new List<String>(skin);


            //  list.setItems();

        }


    }

    private class VariableTable extends Table {
        VariableTable(Skin skin) {
            this.skin = skin;

            items = new Table();
            scroll = new ScrollPane(items);
            Table container = new Table();
            container.add(scroll).expand().fillX().top();
            add(container).fill().expand();
        }

        void addVariable() {
            VariableItem var = new VariableItem(Integer.toString(counter++), skin);
            items.add(var).fillX().expandX();
            items.row();

            scroll.layout();
            scroll.scrollTo(0, 0, 0, 0);
        }

        private Table items;
        private ScrollPane scroll;
        private Skin skin;
        private int counter = 1;
    }

    private static class FunTable extends Table {
        FunTable(Skin skin) {
            this.skin = skin;

            items = new VerticalGroup();

            scroll = new ScrollPane(items);
            Table container = new Table();

            container.add(scroll).expand();
            add(container).fill().expand();
        }

        void addFun() {
        }

        private VerticalGroup items;
        private ScrollPane scroll;
        private Skin skin;
        private static int counter = 1;
    }

    public ControlTable(Skin skin) {

        setWidth(200);
        NinePatch patch = new NinePatch(new Texture(Gdx.files.internal("VarTable.png")), 1, 1, 1, 1);
        setBackground(new NinePatchDrawable(patch));

        top();
        Table tabTable = new Table();

        final Button components = new TextButton("Components", skin);
        final Button variables = new TextButton("Variables", skin);
        final Button functions = new TextButton("Functions", skin);

        ButtonGroup<Button> tabs = new ButtonGroup<Button>();
        tabs.setMinCheckCount(1);
        tabs.setMaxCheckCount(1);
        tabs.add(components);
        tabs.add(variables);
        tabs.add(functions);

        tabTable.add(components).fill().expand();
        tabTable.add(variables).fill().expand();
        tabTable.add(functions).fill().expand();

        add(tabTable).fillX().expandX().height(30);
        row();

        Stack mainContainer = new Stack();
        final ComponentsTable componentsTable = new ComponentsTable(skin);

        mainContainer.add(componentsTable);

        final Table container = new Table();

        Button createNewElement = new TextButton("Create new", skin);
        container.add(createNewElement).fillX().expandX().height(30).row();

        mainContainer.add(container);

        final VariableTable variableTable = new VariableTable(skin);
        final FunTable funTable = new FunTable(skin);

        Stack contents = new Stack();
        contents.add(variableTable);
        contents.add(funTable);

        container.add(contents).fill().expand();
        mainContainer.add(container);
        add(mainContainer).fill().expand();

        ChangeListener tabListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                componentsTable.setVisible(components.isChecked());
                container.setVisible(!components.isChecked());

                variableTable.setVisible(variables.isChecked());
                funTable.setVisible(functions.isChecked());
            }
        };
        components.addListener(tabListener);
        variables.addListener(tabListener);
        functions.addListener(tabListener);

        createNewElement.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (variables.isChecked()) {
                    variableTable.addVariable();
                } else if (functions.isChecked()) {
                    funTable.addFun();
                }
            }
        });

        variables.setChecked(true);
        funTable.setVisible(false);

    }
}
