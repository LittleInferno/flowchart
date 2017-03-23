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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Node extends VisWindow implements CodeGen {

    protected final VerticalGroup left;
    protected final VerticalGroup right;
    private final VisTable container;

    private NodeParams nodeParams;

    public Node(NodeParams nodeParams) {
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
    }

    Node initFromHandle(NodeParams nodeParams) {
        this.nodeParams = nodeParams;
        setPosition(nodeParams.x, nodeParams.y);
        setName(nodeParams.title);
        setTitle(nodeParams.title);
        if (nodeParams.closable) {
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

    public NodeParams getHandle() {
        nodeParams.setX(getX());
        nodeParams.setY(getY());
        return nodeParams;
    }

    public String getId() {
        return nodeParams.id;
    }

    @Override
    public Scene getStage() {
        return (Scene) super.getStage();
    }

    @SuppressWarnings("WeakerAccess")
    static public class NodeParams {
        private float x, y;
        private String title;
        private String type;
        private String id;
        private boolean closable;

        private Map<String, Object> attributes;

        NodeParams() {

        }

        public NodeParams(String title, boolean closable) {
            this(title, closable, null);
        }

        public NodeParams(String title, boolean closable, String type) {
            this(0, 0, title, type, closable, new HashMap<>());
        }

        public NodeParams(float x, float y, String title, String type, boolean closable, Map<String, Object> attributes) {
            this.x = x;
            this.y = y;
            this.title = title;
            this.type = type;
            this.attributes = attributes;
            this.id = Project.createID().toString();
            this.closable = closable;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public String getTitle() {
            return title;
        }

        public String getType() {
            return type;
        }

        public String getId() {
            return id;
        }

        public boolean isClosable() {
            return closable;
        }

        public void addAttr(String key, Object attr) {
            attributes.put(key, attr);
        }

        public Object getAttr(String key) {
            return attributes.get(key);
        }

        public NodeParams setX(float x) {
            this.x = x;
            return this;
        }

        public NodeParams setY(float y) {
            this.y = y;
            return this;
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
