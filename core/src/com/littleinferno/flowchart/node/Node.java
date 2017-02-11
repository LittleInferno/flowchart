package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.CodeGen;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.ui.Main;

public abstract class Node extends VisWindow implements CodeGen {

    protected final VerticalGroup left;
    protected final VerticalGroup right;
    final NodeStyle style;
    Skin skin;

    public Node(final String name, final boolean closable) {
        this(name, closable, Main.skin);
    }

    public Node(final String name, final boolean closable, Skin skin) {
        super(name);
        setKeepWithinStage(false);
        setKeepWithinParent(false);


        style = skin.get(NodeStyle.class);
        this.skin = skin;
        setWidth(200);

        if (closable) {
            addCloseButton();
        }

        VisTable container = new VisTable();
        VisTable main = new VisTable();
        main.addSeparator();

        left = new VerticalGroup();
        left.top().left().fill();
        left.space(10);
        container.add(left).grow().width(100);

        container.addSeparator(true);

        right = new VerticalGroup();
        right.top().right().fill();
        right.space(10);
        container.add(right).grow().width(100);

        main.add(container).grow();

        add(main).grow();
        top();

    }

    public Pin addDataInputPin(final String name, DataType... possibleConvert) {
        Pin pin = new Pin(this, name, Connection.INPUT, possibleConvert);
        left.addActor(pin);
        pack();
        return pin;
    }

    public Pin addDataInputPin(final DataType type, final String name) {
        Pin pin = new Pin(this, name, type, Connection.INPUT);
        left.addActor(pin);
        pack();
        return pin;
    }

    public Pin addDataOutputPin(final String name, DataType... possibleConvert) {
        Pin pin = new Pin(this, name, Connection.OUTPUT, possibleConvert);
        right.addActor(pin);
        pack();
        return pin;
    }

    public Pin addDataOutputPin(final DataType type, final String name) {
        Pin pin = new Pin(this, name, type, Connection.OUTPUT);
        right.addActor(pin);
        pack();
        return pin;
    }


    public Pin addExecutionInputPin() {
        return addExecutionInputPin("exec in");
    }

    public Pin addExecutionInputPin(final String name) {
        Pin pin = new Pin(this, name, DataType.EXECUTION, Connection.INPUT);
        left.addActor(pin);
        pack();
        return pin;
    }

    public Pin addExecutionOutputPin() {
        return addExecutionOutputPin("exec out");
    }

    public Pin addExecutionOutputPin(final String name) {
        Pin pin = new Pin(this, name, DataType.EXECUTION, Connection.OUTPUT);
        right.addActor(pin);
        pack();
        return pin;
    }

    public void removePin(final String name) {
        left.removeActor(left.findActor(name));
        right.removeActor(right.findActor(name));
    }

    public void removePin(final Pin pin) {
        left.removeActor(pin);
        right.removeActor(pin);
    }

    @Deprecated
    public Pin getPin(String name) {
        return (Pin) findActor(name);
    }

    public Array<Pin> getInput() {
        Array<Actor> children = left.getChildren();

        Array<Pin> result = new Array<Pin>(children.size);

        for (Actor i : children) result.add((Pin) i);

        return result;
    }

    public Array<Pin> getOutput() {
        Array<Actor> children = right.getChildren();

        Array<Pin> result = new Array<Pin>(children.size);

        for (Actor i : children) result.add((Pin) i);

        return result;
    }

    public void setTitle(String text) {
        getTitleLabel().setText(text);
    }

    public void removeCloseButton() {
        Array<Cell> cells = getTitleTable().getCells();
        cells.get(cells.size - 1).clearActor();
        cells.removeIndex(cells.size - 1);
    }

    @Override
    public void close() {

        SnapshotArray<Actor> leftChildren = left.getChildren();

        for (Actor actor : leftChildren) {
            if (actor instanceof Pin) {
                ((Pin) actor).disconnect();
            }
        }

        SnapshotArray<Actor> rightChildren = right.getChildren();

        for (Actor actor : rightChildren) {
            if (actor instanceof Pin) {
                ((Pin) actor).disconnect();
            }
        }

        super.close();
    }

    static public class NodeStyle {

        public Drawable normal, select;

        public NodeStyle() {
        }

        public NodeStyle(Drawable normal, Drawable select) {
            this.normal = normal;
            this.select = select;

        }
    }
}
