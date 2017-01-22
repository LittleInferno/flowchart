package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.littleinferno.flowchart.Function;
import com.littleinferno.flowchart.node.FunctionCallNode;
import com.littleinferno.flowchart.node.FunctionReturnNode;
import com.littleinferno.flowchart.parameter.InputParameter;
import com.littleinferno.flowchart.parameter.OutputParameter;
import com.littleinferno.flowchart.parameter.Parameter;
import com.littleinferno.flowchart.value.Value;


class FunctionItem extends Item {

    FunctionItem(final String funName, final Skin skin) {

        function = new Function(funName);

        Table title = new Table();

        final Label name = new Label(funName, skin);
        name.setEllipsis(true);
        title.add(name).expand().fill()
                .width(151);// TODO WTF? Fix it

        final Button edit = new TextButton("ed", skin);
        title.add(edit).size(30).right();

        final Button add = new TextButton("add", skin);
        title.add(add).size(30).right();

        final Button del = new TextButton("del", skin);
        title.add(del).size(30).right();

        final Button ret = new TextButton("ret", skin);
        title.add(ret).size(30).right();

        Tree tree = new Tree(skin);

        Tree.Node titleNode = new Tree.Node(title);
        titleNode.setSelectable(false);


        Table property = new Table();
        Tree.Node propertyNode = new Tree.Node(property);
        propertyNode.setSelectable(false);

        property.add(new Label("name", skin)).expand().fillX().height(30).row();
        final TextField textField = new TextField(funName, skin);

        property.add(textField).expand().fillX().height(30).row();
        Tree propertyTree = new Tree(skin);

        property.add(propertyTree).fill().expand();
        Table inputTitle = new Table();
        inputTitle.add(new Label("input", skin)).expand().fill()
                .width(161);// TODO WTF? Fix it

        final Button addInput = new TextButton("add", skin);
        inputTitle.add(addInput).size(30).right();


        Tree.Node inputTitleNode = new Tree.Node(inputTitle);
        inputTitleNode.setSelectable(false);

        final VerticalGroup inputList = new VerticalGroup();
        Tree.Node inputNode = new Tree.Node(inputList);
        inputNode.setSelectable(false);

        inputTitleNode.add(inputNode);


        Table outputTitle = new Table();

        outputTitle.add(new Label("output", skin)).expand().fill()
                .width(161);// TODO WTF? Fix it

        final Button addOutput = new TextButton("add", skin);
        outputTitle.add(addOutput).size(30).right();

        Tree.Node outputTitleNode = new Tree.Node(outputTitle);
        outputTitleNode.setSelectable(false);

        final VerticalGroup outputList = new VerticalGroup();
        Tree.Node outputNode = new Tree.Node(outputList);
        outputNode.setSelectable(false);

        outputTitleNode.add(outputNode);


        addInput.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                final Parameter parameter = new InputParameter(function, Integer.toString(counter++), Value.Type.BOOL);

                final Item.PropertyTable propertyItem = new Item.PropertyTable(parameter.getName(), skin);

                Button button = new TextButton("delete", skin);

                propertyItem.add(button).fillX().expandX();

                inputList.addActor(propertyItem);

                propertyItem.addTextEnteredListener(new Item.PropertyTable.TextEntered() {
                    @Override
                    public void entered(String text) {
                        parameter.setName(text);
                    }
                });

                propertyItem.addTypeSelectedListener(new Item.PropertyTable.TypeSelected() {
                    @Override
                    public void select(Value.Type type) {
                        parameter.setValueType(type);
                    }
                });

                button.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        function.removeParameter(parameter);
                    }
                });
            }
        });

        addOutput.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                final Parameter parameter = new OutputParameter(function, Integer.toString(counter++), Value.Type.BOOL);

                final Item.PropertyTable propertyItem = new Item.PropertyTable(parameter.getName(), skin);
                outputList.addActor(propertyItem);

                propertyItem.addTextEnteredListener(new Item.PropertyTable.TextEntered() {
                    @Override
                    public void entered(String text) {
                        parameter.setName(text);
                    }
                });

                propertyItem.addTypeSelectedListener(new Item.PropertyTable.TypeSelected() {
                    @Override
                    public void select(Value.Type type) {
                        parameter.setValueType(type);
                    }
                });
            }
        });

        Main.addSource(new DragAndDrop.Source(add) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                payload.setObject(new FunctionCallNode(function, skin));

                payload.setDragActor(new Label(String.format("call %s", function.getName()), skin));

                return payload;
            }
        });

        textField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                function.setName(textField.getText());
            }
        });

        del.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //   FunctionItem.this.getParent().removeActor(FunctionItem.this);
            }
        });


        Main.addSource(new DragAndDrop.Source(ret) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                FunctionReturnNode returnNode = new FunctionReturnNode(function, skin);

                function.getReturnNodes().add(returnNode);


                payload.setObject(returnNode);

                payload.setDragActor(new Label("return node", skin));

                return payload;
            }
        });


        propertyTree.add(inputTitleNode);
        propertyTree.add(outputTitleNode);

        titleNode.add(propertyNode);
        tree.add(titleNode);
        add(tree).fillX().expandX();


    }

    private int counter = 1;
    Function function;
}
