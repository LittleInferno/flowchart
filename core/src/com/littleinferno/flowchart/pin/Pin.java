package com.littleinferno.flowchart.pin;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.ui.Main;
import com.littleinferno.flowchart.wire.WireManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

public class Pin extends VisTable {
    private PinStyle style;

    private Pin connectedPin;
    private ArrayList<Pin> connectedPins = new ArrayList<Pin>();

    private Connection connection;
    private DataType type;
    private boolean isConnect;
    private boolean isArray;
    private boolean isUniversal;
    private int pinId = -1;

    private VisLabel label;
    private VisImage image;

    private Node parent;

    private ArrayList<PinListener> listeners = new ArrayList<PinListener>();

    private HashSet<DataType> possibleConvert = new HashSet<DataType>();

    public Pin(Node parent, String name, Connection connection, DataType... convert) {
        Collections.addAll(possibleConvert, convert);
        possibleConvert.add(DataType.UNIVERSAL);
        init(parent, name, connection, DataType.UNIVERSAL);
    }

    public Pin(Node parent, String name, DataType type, Connection connection) {
        possibleConvert.add(type);
        init(parent, name, connection, type);
    }

    private void init(Node parent, String name, Connection connection, DataType type) {
        this.parent = parent;
        this.connection = connection;
        this.style = Main.skin.get(Main.scale == VisUI.SkinScale.X1 ? "X1" : "X2", PinStyle.class);

        label = new VisLabel(name);
        label.setEllipsis(true);
        label.setAlignment(Align.center);

        image = new VisImage();

        setArray(false);
        setType(type);

        if (connection == Connection.INPUT) {
            add(image);
            add(label).growX().minWidth(0);
        } else {
            add(label).growX().minWidth(0);
            add(image);
        }

        setName(name);

        isUniversal = type == DataType.UNIVERSAL;

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                WireManager.add(Pin.this);
                return true;
            }
        });
    }

    public void setStyle(PinStyle style) {
        if (style == null) throw new IllegalArgumentException("style cannot be null.");
        this.style = style;
        label.setStyle(new Label.LabelStyle(style.font, style.fontColor));
    }

    public PinStyle getStyle() {
        return style;
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

        if (!possibleConvert.contains(newType))
            return false;

        type = newType;

        switch (newType) {
            case EXECUTION:
                image.setColor(style.execution);
                break;
            case BOOL:
                image.setColor(style.bool);
                break;
            case INT:
                image.setColor(style.integer);
                break;
            case FLOAT:
                image.setColor(style.floating);
                break;
            case STRING:
                image.setColor(style.string);
                break;
            case UNIVERSAL:
                image.setColor(style.universal);
                break;
        }

        if (isUniversal) {
            notifyListenersTypeChanged(newType);
            if (connectedPin != null) connectedPin.setType(newType);

            if (!connectedPins.isEmpty()) {
                for (Pin i : connectedPins) {
                    i.setType(newType);
                }
            }
        }

        return true;
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

        if (isExecutionOutput(this) || isDataInput(this))
            connectPin(pin);
        else
            connectPins(pin);

        return true;
    }

    public void disconnect(final Pin pin) {
        if (isExecutionOutput(this) || isDataInput(this)) {
            disconnectPin(pin);
        } else {
            disconnectPins(pin);
        }
    }

    private void connectPin(final Pin pin) {
        if (isConnect())
            connectedPin.disconnect(this);

        connectedPin = pin;
        connectedPin.connectedPins.add(this);

        pinId = WireManager.instance.add(this, pin);
        isConnect = true;
    }

    private void connectPins(final Pin pin) {
        pin.connectPin(this);
        isConnect = true;
    }

    private void disconnectPin(final Pin pin) {
        isConnect = false;
        connectedPin.connectedPins.remove(this);
        connectedPin = null;
        pinId = WireManager.instance.remove(pinId);
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
        if (isConnect()) {
            if (isExecutionOutput(this) || isDataInput(this)) {
                disconnectPin(connectedPin);
            } else {
                for (Pin pin : connectedPins)
                    pin.disconnect(this);
            }
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

    private boolean isExecutionOutput(Pin pin) {
        return pin.getType() == DataType.EXECUTION && pin.getConnection() == Connection.OUTPUT;
    }

    private boolean isDataInput(Pin pin) {
        return pin.getType() != DataType.EXECUTION && pin.getConnection() == Connection.INPUT;
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
//            converter = new ConverterNode(second.getType(), first.getType(), getSkin());
//            converter.getPin("from").connect(second);
//            converter.getPin("to").connect(first);
//        } else {
//            converter = new ConverterNode(first.getType(), second.getType(), getSkin());
//            converter.getPin("from").connect(first);
//            converter.getPin("to").connect(second);
//        }
//        converter.setPosition(pos.x, pos.y);
//        getStage().addActor(converter);
//    }

    public void addListener(PinListener listener) {
        listeners.add(listener);
    }


    private void notifyListenersTypeChanged(DataType newType) {
        for (PinListener listener : listeners) {
            listener.typeChanged(newType);
        }
    }

    static public class Connector {

        public final Node parent;
        public final Pin pin;

        Connector(Pin pin) {
            this.pin = pin;

            this.parent = pin.parent;
        }

    }


    public static class PinStyle {
        Drawable array, arrayConnected, pin, pinConnected;
        Color execution;
        Color bool;
        Color integer;
        Color floating;
        Color string;

        Color universal;
        BitmapFont font;
        Color fontColor;

        public PinStyle() {
        }

        public PinStyle(Drawable array, Drawable arrayConnected, Drawable pin,
                        Drawable pinConnected, Color execution, Color bool,
                        Color integer, Color floating, Color string, Color universal,
                        BitmapFont font, Color fontColor) {
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
            this.font = font;
            this.fontColor = fontColor;
        }
    }
}
