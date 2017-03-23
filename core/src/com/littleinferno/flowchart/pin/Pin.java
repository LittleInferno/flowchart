package com.littleinferno.flowchart.pin;

import com.annimon.stream.Stream;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.scene.Scene;
import com.littleinferno.flowchart.wire.WireManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Pin extends VisTable {
    public static final DataType[] DEFAULT_CONVERT =
            {DataType.BOOL, DataType.INT, DataType.FLOAT, DataType.STRING};

    private PinStyle style;

    private Connection connection;
    private DataType type;
    private boolean isConnect;
    private boolean isArray;

    private boolean isUniversal;
    private VisLabel label;
    private VisImage image;

    private Node parent;
    private GestureDetector gestureDetector;

    private Pin connectedPin;
    private List<Pin> connectedPins;
    private Set<DataType> possibleConvert;

    private int wireId = WireManager.NULL_ID;
    private Color typeColor;

    public Pin(Pin other) {
        setStyle(other.style);
        this.possibleConvert = other.possibleConvert;
        init(other.parent, other.getName(), other.connection, other.type);
        this.isArray = other.isArray;
    }

    public Pin(Node parent, String name, Connection connection, DataType... convert) {
        possibleConvert = new HashSet<>();

        setStyle(VisUI.getSkin().get(PinStyle.class));

        if (convert.length == 1) {
            possibleConvert.addAll(Arrays.asList(DEFAULT_CONVERT));
            init(parent, name, connection, convert[0]);
        } else {
            Collections.addAll(possibleConvert, convert);
            possibleConvert.add(DataType.UNIVERSAL);
            init(parent, name, connection, DataType.UNIVERSAL);
        }
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    private void init(final Node parent, String name, final Connection connection, final DataType type) {
        this.parent = parent;
        this.connection = connection;
        this.isUniversal = type == DataType.UNIVERSAL;
        this.connectedPins = new ArrayList<>();
        this.gestureDetector = new GestureDetector(new PinGesture());

        this.label = new VisLabel();
        this.label.setEllipsis(true);
        this.label.setAlignment(Align.center);

        this.image = new VisImage();

        setName(name);
        setArray(false);
        setType(type);

        if (connection == Connection.INPUT) {
            add(image).minWidth(image.getWidth()).minHeight(image.getHeight());
            add(label).growX().minWidth(0);
            label.setAlignment(Align.left);
        } else {
            add(label).growX().minWidth(0);
            add(image).minWidth(image.getWidth()).minHeight(image.getHeight());
            label.setAlignment(Align.right);
        }

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                gestureDetector.touchDown(x, y, pointer, 0);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                gestureDetector.touchUp(x, y, pointer, 0);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                gestureDetector.touchDragged(x, y, pointer);
            }
        });
    }


    private void setStyle(PinStyle style) {
        if (style == null) throw new IllegalArgumentException("style cannot be null.");
        this.style = style;
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        label.setText(name);
    }

    public DataType getType() {
        return type;
    }

    public boolean setType(DataType newType) {
        if (newType == type) return true;

        if (!possibleConvert.contains(newType) && newType != DataType.EXECUTION)
            return false;

        type = newType;

        switch (newType) {
            case EXECUTION:
                typeColor = style.execution;
                break;
            case BOOL:
                typeColor = style.bool;
                break;
            case INT:
                typeColor = style.integer;
                break;
            case FLOAT:
                typeColor = style.floating;
                break;
            case STRING:
                typeColor = style.string;
                break;
            case UNIVERSAL:
                typeColor = style.universal;
                break;
        }

        image.setColor(typeColor);

        if (isUniversal) {

            if (connectedPin != null) connectedPin.setType(newType);

            Stream.of(connectedPin)
                    .forEach(pin -> setType(newType));

            if (parent != null)
                Stream.of(parent.getPins())
                        .filter(Pin::isUniversal)
                        .forEach(pin -> pin.setType(newType));
        }

        return true;
    }

    public boolean isUniversal() {
        return isUniversal;
    }

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean array) {
        isArray = array;

        if (isArray)
            image.setDrawable(style.array);
        else
            image.setDrawable(style.pin);
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public boolean connect(final Pin pin) {

        if (!possibleConnect(pin))
            return false;

        DataType pinType = pin.getType();
        DataType thisType = getType();

        if (pinType != thisType) {
            if (pinType == DataType.UNIVERSAL || thisType == DataType.UNIVERSAL) {
                boolean success;

                success = pinType == DataType.UNIVERSAL ?
                        pin.setType(thisType) :
                        setType(pinType);

                if (!success) return false;
            } else
                return false;
        }

        if (isSingle())
            connectPin(pin);
        else
            connectPins(pin);

        return true;
    }

    public void disconnect(final Pin pin) {
        if (isSingle()) {
            disconnectPin();
        } else {
            disconnectPins(pin);
        }
    }

    private void connectPin(final Pin pin) {
        if (isConnect())
            connectedPin.disconnect(this);

        connectedPin = pin;
        connectedPin.connectedPins.add(this);

        wireId = getStage().getWireManager().add(this, pin);
        isConnect = true;
    }

    private void connectPins(final Pin pin) {
        pin.connectPin(this);
        isConnect = true;
    }

    private void disconnectPin() {
        isConnect = false;
        connectedPin.connectedPins.remove(this);
        connectedPin = null;
        wireId = getStage().getWireManager().remove(wireId);
    }

    @Override
    public Scene getStage() {
        return (Scene) super.getStage();
    }

    private void disconnectPins(final Pin pin) {
        pin.disconnect(this);

        for (Iterator<Pin> i = connectedPins.iterator(); i.hasNext(); ) {
            if (i.next() == pin)
                i.remove();
        }

        if (connectedPins.isEmpty()) isConnect = false;
    }

    public void disconnect() {
        if (isSingle() && isConnect()) {
            disconnectPin();
        } else {
            for (int i = 0; i < connectedPins.size(); ++i)
                connectedPins.get(i).disconnect(this);
        }
    }

    public Vector2 getLocation() {
        return localToStageCoordinates(new Vector2(0, 0));
    }

    public Connector getConnector() {
        if (connectedPin != null)
            return new Connector(connectedPin);
        return null;
    }

    private boolean possibleConnect(Pin pin) {
        return !(pin.getConnection() == getConnection()
                || pin.getParent() == getParent()
                || pin.isArray() != isArray());
    }

    private boolean isMultiple() {
        return (getType() == DataType.EXECUTION && getConnection() == Connection.INPUT) ||
                (getType() != DataType.EXECUTION && getConnection() == Connection.OUTPUT);
    }

    private boolean isSingle() {
        return (getType() == DataType.EXECUTION && getConnection() == Connection.OUTPUT) ||
                (getType() != DataType.EXECUTION && getConnection() == Connection.INPUT);
    }

//    private void createConverter(Pin first, Pin second) {
//
//        Vector2 pos;
//        pos = getLocation().add(first.getLocation());
//        pos.x /= 2;
//        pos.y /= 2;
//
//        ConverterNode converter;
//        if (getConnection() == Connection.INPUT.INPUT) {
//            converter = new ConverterNode(second.getOperator(), first.getOperator(), getSkin());
//            converter.getPin("from").connect(second);
//            converter.getPin("to").connect(first);
//        } else {
//            converter = new ConverterNode(first.getOperator(), second.getOperator(), getSkin());
//            converter.getPin("from").connect(first);
//            converter.getPin("to").connect(second);
//        }
//        converter.setPosition(pos.x, pos.y);
//        getStage().addActor(converter);
//    }

    public Pin getConnectedPin() {
        return connectedPin;
    }

    public Node getNode() {
        return parent;
    }

    public Color getTypeColor() {
        return typeColor;
    }

    static public class Connector {

        public final Node parent;
        public final Pin pin;

        Connector(Pin pin) {
            this.pin = pin;

            this.parent = pin.parent;
        }

    }

    public String generate(BaseCodeGenerator generator) {
        Pin connectedPin = this.getConnectedPin();
        return connectedPin.getNode().gen(generator, connectedPin);
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public static class PinStyle {
        Drawable array, arrayConnected, pin, pinConnected;
        Color execution;
        Color bool;
        Color integer;
        Color floating;
        Color string;
        Color universal;


        public PinStyle() {
        }

        public PinStyle(Drawable array, Drawable arrayConnected, Drawable pin,
                        Drawable pinConnected, Color execution, Color bool,
                        Color integer, Color floating, Color string, Color universal) {
            this.array = array;
            this.arrayConnected = arrayConnected;
            this.pin = pin;
            this.pinConnected = pinConnected;
            this.execution = execution;
            this.bool = bool;
            this.integer = integer;
            this.floating = floating;
            this.string = string;
            this.universal = universal;
        }
    }


    private class PinGesture extends GestureDetector.GestureAdapter {

        @Override
        public boolean tap(float x, float y, int count, int button) {
            getStage().getWireManager().add(Pin.this);
            return true;
        }

        @Override
        public boolean longPress(float x, float y) {
            PinPopumMenu pinPopumMenu = new PinPopumMenu();

            Vector2 vec = localToStageCoordinates(new Vector2());
            pinPopumMenu.setPosition(vec.x, vec.y);
            getStage().addActor(pinPopumMenu);

            return true;
        }
    }

    private class PinPopumMenu extends PopupMenu {
        PinPopumMenu() {
            addItem(new MenuItem("break connection", new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Pin.this.disconnect();
                }
            }));
        }
    }

    public class PinHandle {


    }
}
