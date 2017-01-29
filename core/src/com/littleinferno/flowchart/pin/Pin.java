package com.littleinferno.flowchart.pin;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.node.ConverterNode;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.wire.Wire;
import com.littleinferno.flowchart.wire.WireConnector;

public class Pin extends Table {
    private PinStyle style;

    private Pin pin;
    private Array<Pin> pins = new Array<Pin>();
    private final Connection connection;
    private DataType type;
    private boolean isConnect;
    private boolean isArray;

    private Label label;
    private Image image;

    public Pin(String name, DataType type, Connection connection, Skin skin) {
        super(skin);

        this.connection = connection;
        this.style = skin.get(PinStyle.class);

        label = new Label(name, new Label.LabelStyle(style.font, style.fontColor));
        label.setEllipsis(true);
        label.setAlignment(Align.center);
        image = new Image();

        setArray(false);
        setType(type);
        add().minWidth(0);
        if (connection == Connection.INPUT) {
            add(image).size(16);
            add(label).expandX().fillX().minWidth(0);
        } else {
            add(label).expandX().fillX().minWidth(0);
            add(image).size(16);
        }
        setSize(getPrefWidth(), getPrefHeight());
        setName(name);


        image.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                WireConnector.add(Pin.this);
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

    public void setType(DataType type) {
        this.type = type;

        switch (type) {
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
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public boolean connect(Pin pin) {

        if (!possibleConnect(pin))
            return false;

        if (getType() != pin.getType()) {
            if (getType() == DataType.EXECUTION || pin.getType() == DataType.EXECUTION)
                return false;

            createConverter(this, pin);

            return true;
        }

        if (isExecutionOutput(this) || isDataInput(this)) {

            if (isConnect()) pin.disconnect(this);

            this.pin = pin;
            this.pin.pins.add(this);

            WireConnector.base.addActor(new Wire(this, pin));

            return isConnect = true;

        } else {
            return pin.connect(this);
        }
    }

    public void disconnect(Pin pin) {

        if (isExecutionOutput(this) || isDataInput(this)) {
            this.pin = null;
            isConnect = false;
        } else {
            pin.disconnect(this);

            for (int i = 0; i < pins.size; ++i) {
                if (pins.get(i) == pin) {
                    pins.removeIndex(i);
                    break;
                }
            }
        }
    }

    public Vector2 getLocation() {
        return localToStageCoordinates(new Vector2(0, 0));
    }

    public Connector getConnector() {
        if (pin != null)
            return new Connector(pin);
        return null;
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

    private void createConverter(Pin first, Pin second) {

        Vector2 pos;
        pos = getLocation().add(first.getLocation());
        pos.x /= 2;
        pos.y /= 2;

        ConverterNode converter;
        if (getConnection() == Connection.INPUT.INPUT) {
            converter = new ConverterNode(second.getType(), first.getType(), getSkin());
            converter.getPin("from").connect(second);
            converter.getPin("to").connect(first);
        } else {
            converter = new ConverterNode(first.getType(), second.getType(), getSkin());
            converter.getPin("from").connect(first);
            converter.getPin("to").connect(second);
        }
        converter.setPosition(pos.x, pos.y);
        getStage().addActor(converter);
    }


    static public class Connector {

        public final Node parent;
        public final Pin pin;

        Connector(Pin pin) {
            this.pin = pin;

            this.parent = (Node) pin.getParent().getParent();
        }

    }


    static public class PinStyle {
        public Drawable array, pin;
        public Color execution, bool, integer, floating, string;
        public BitmapFont font;
        public Color fontColor;

        public PinStyle() {
        }

        public PinStyle(Drawable array, Drawable pin, Color execution, Color bool, Color integer,
                        Color floating, Color string, BitmapFont font, Color fontColor) {
            this.array = array;
            this.pin = pin;
            this.execution = execution;
            this.bool = bool;
            this.integer = integer;
            this.floating = floating;
            this.string = string;
            this.font = font;
            this.fontColor = fontColor;
        }
    }
}
