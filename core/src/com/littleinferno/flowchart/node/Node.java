package com.littleinferno.flowchart.node;

import com.annimon.stream.Stream;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.CodeGen;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.project.Project;
import com.littleinferno.flowchart.scene.Scene;

import java.util.List;

public abstract class Node extends VisWindow implements CodeGen {

    protected final VerticalGroup left;
    protected final VerticalGroup right;
    private final VisTable container;

    private NodeHandle nodeHandle;

    public Node(NodeHandle nodeHandle) {
        super("");

        left = new VerticalGroup();
        right = new VerticalGroup();
        container = null;
    }

    public Node() {
        super("");
        getTitleLabel().setAlignment(Align.center);

        setKeepWithinStage(false);
        setKeepWithinParent(false);

        // TODO move to style
        if (Gdx.app.getType() == Application.ApplicationType.Android)
            setWidth(400);
        else
            setWidth(200);

        container = new VisTable();
        VisTable main = new VisTable();

        left = new VerticalGroup();
        left.top().left().fill();
        left.space(10);
        container.add(left).grow().width(getWidth() / 2);

        right = new VerticalGroup();
        right.top().right().fill();
        right.space(10);
        container.add(right).grow().width(getWidth() / 2).row();

        main.add(container).grow().row();

        add(main).grow();
        top();


        container.setDebug(true, true);
left.setDebug(true,true);
        right.setDebug(true,true);

    }

    Node initFromHandle(NodeHandle nodeHandle) {
        this.nodeHandle = nodeHandle;
        setPosition(nodeHandle.x, nodeHandle.y);
        setName(nodeHandle.title);
        setTitle(nodeHandle.title);
        if (nodeHandle.closable) {
            addCloseButton();
        }
        return this;
    }

    public Pin addDataInputPin(final String name, DataType... possibleConvert) {
        Pin pin = new Pin(this, name, Connection.INPUT, possibleConvert);
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

    public Pin addExecutionInputPin() {
        return addExecutionInputPin("exec in");
    }

    public Pin addExecutionInputPin(final String name) {
        Pin pin = new Pin(this, name, Connection.INPUT, DataType.EXECUTION);
        left.addActor(pin);
        pack();
        return pin;
    }

    public Pin addExecutionOutputPin() {
        return addExecutionOutputPin("exec out");
    }

    public Pin addExecutionOutputPin(final String name) {
        Pin pin = new Pin(this, name, Connection.OUTPUT, DataType.EXECUTION);
        right.addActor(pin);
        pack();
        return pin;
    }

    public void addPins(Pin[] pins) {
        Stream.of(pins)
                .peek(p -> p.setParent(this))
                .forEach(
                        pin -> {
                            if (pin.getConnection() == Connection.INPUT)
                                left.addActor(pin);
                            else
                                right.addActor(pin);
                        }
                );
        pack();
    }

    public void removePin(final Pin pin) {
        left.removeActor(pin);
        right.removeActor(pin);
    }

    public void setTitle(String text) {
        getTitleLabel().setText(text);
    }

    public void removeCloseButton() {
        Array<Cell> cells = getTitleTable().getCells();
        cells.get(cells.size - 1).clearActor();
        cells.removeIndex(cells.size - 1);
    }

    public Pin getPin(String name) {
        SnapshotArray<Actor> leftChildren = left.getChildren();

        for (Actor actor : leftChildren) {
            if (actor instanceof Pin) {
                if (actor.getName().equals(name))
                    return (Pin) actor;
            }
        }

        SnapshotArray<Actor> rightChildren = right.getChildren();

        for (Actor actor : rightChildren) {
            if (actor instanceof Pin) {
                if (actor.getName().equals(name))
                    return (Pin) actor;
            }
        }
        return null;
    }

    public List<Pin> getPins() {

        List<Pin> pins = Stream.of(left.getChildren())
                .filter(value -> value instanceof Pin)
                .map(Pin.class::cast)
                .toList();

        pins.addAll(Stream.of(right.getChildren())
                .filter(value -> value instanceof Pin)
                .map(Pin.class::cast)
                .toList());

        return pins;
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

        getStage().getNodeManager().deleteNode(this);

        super.close();
    }

    public VisTable getContainer() {
        return container;
    }

    public NodeHandle getHandle() {
        nodeHandle.x = getX();
        nodeHandle.y = getY();
        return nodeHandle;
    }

    public String getId() {
        return nodeHandle.id;
    }

    @Override
    public Scene getStage() {
        return (Scene) super.getStage();
    }

    @SuppressWarnings("WeakerAccess")
    static public class NodeHandle {
        public float x, y;
        public String title;
        public String name;
        public String id;
        public boolean closable;

        public NodeHandle() {
        }

        public NodeHandle(String title, boolean closable) {
            this(title, closable, null);
        }

        public NodeHandle(String title, boolean closable, String name) {
            this(0, 0, title, name, closable);
        }

        public NodeHandle(float x, float y, String title, String name, boolean closable) {
            this.x = x;
            this.y = y;
            this.title = title;
            this.name = name;
            this.id = Project.createID().toString();
            this.closable = closable;
        }
    }

    static public class NodeStyle {

        int width;

        public NodeStyle() {
        }

        public NodeStyle(int width) {
            this.width = width;
        }
    }
}
