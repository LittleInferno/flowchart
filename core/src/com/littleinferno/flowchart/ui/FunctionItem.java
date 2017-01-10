package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.value.Value;


public class FunctionItem extends Table {

    static class Item extends Table {
        Item(Skin skin) {
            setSize(200, 30);
            TextField textField = new TextField("", skin);
            add(textField).width(80).height(30);

            SelectBox selectBox = new SelectBox(skin);
            selectBox.setItems(Value.Type.BOOL,
                    Value.Type.FLOAT,
                    Value.Type.INT,
                    Value.Type.STRING);

            add(selectBox).width(70).height(30);

            Button b = new Button(skin);
            b.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    ((VerticalGroup) actor.getParent().getParent()).removeActor(actor.getParent());
                }
            });

            add(b).size(30);
        }
    }

    public FunctionItem(Skin skin) {
        this.skin = skin;

        setWidth(200);
        setHeight(270);

        HorizontalGroup hg = new HorizontalGroup();
        functionName = new TextField("", skin);
        hg.addActor(functionName);
        hg.addActor(new Label("function name", skin));

        add(hg).fillX().expandX().height(30).colspan(2);
        row();

        Table inputTitleTable = new Table();

        final Button inputSwitch = new Button(skin);

        inputTitleTable.add(inputSwitch).size(30).left();

        inputTitleTable.add(new Label("input", this.skin)).fillX().expandX().height(30);

        Button addInputItem = new Button(skin);

        inputTitleTable.add(addInputItem).size(30).right();

        add(inputTitleTable).fillX().expandX().height(30);

        row();

        inputGroup = new VerticalGroup();
        inputGroup.top();
        inputScrollPane = new ScrollPane(inputGroup);

        Table container = new Table();
        container.add(inputScrollPane).expand().top().left();
        container.setName("in");
        add(container).height(120).fill().expand();


        inputSwitch.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                inputScrollPane.setVisible(inputSwitch.isChecked());
            }
        });

        addInputItem.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                addInput();
            }
        });


        row();

        outputGroup = new VerticalGroup();
        outputScrollPane = new ScrollPane(outputGroup);
        initTree(outputGroup, outputScrollPane, "output");

        Button button2 = new Button(skin);

        button2.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                addOutput();
            }
        });
        //  add(button2).size(30).top();

        row();

        pack();
        //    addInput();
        //   addInput();
        addOutput();
        addOutput();
    }


    private void addInput() {
        inputGroup.addActor(new Item(skin));
        inputScrollPane.layout();
        inputScrollPane.scrollTo(0, 0, 0, 0);
    }

    private void addOutput() {
        outputGroup.addActor(new Item(skin));
        outputScrollPane.layout();
        outputScrollPane.scrollTo(0, 0, 0, 0);
    }


    private void initTree(VerticalGroup group, ScrollPane scrollPane, CharSequence name) {

        Tree tree = new Tree(this.skin);
        //tree.set
        Tree.Node list = new Tree.Node(new Label(name, this.skin));
        list.setSelectable(false);

        Table container = new Table();
        container.add(scrollPane).expandX().fillX().height(90);
        container.setHeight(120);
        Tree.Node in = new Tree.Node(container);
        in.setSelectable(false);

        list.add(in);

        tree.add(list);
        tree.setPadding(0);
        tree.layout();
        this.add(tree).fillX().expandX().height(120);
    }

    private Skin skin;

    private TextField functionName;

    private VerticalGroup inputGroup;
    private ScrollPane inputScrollPane;

    private VerticalGroup outputGroup;
    private ScrollPane outputScrollPane;


    private Table input;
}
