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
import com.littleinferno.flowchart.node.ConverterNode;
import com.littleinferno.flowchart.node.FunctionCallNode;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.value.Value;
import com.littleinferno.flowchart.wire.Wire;
import com.littleinferno.flowchart.wire.WireConnector;

public class Pin extends Table {
    private PinStyle style;

    private Pin pin;
    private Array<Pin> pins = new Array<Pin>();
    private final int connection;
    private Value.Type type;
    private Value value;
    private boolean isConnect;

    private Label label;
    private Image image;

    static public final int input = 0, output = 1;

    public Pin(String name, Value.Type type, int connection, Skin skin) {
        super(skin);

        this.connection = connection;
        this.style = skin.get(PinStyle.class);

        label = new Label(name, new Label.LabelStyle(style.font, style.fontColor));
        label.setEllipsis(true);
        label.setAlignment(Align.center);
        image = new Image();

        setType(type);
        add().minWidth(0);
        if (connection == input) {
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

    public Value.Type getType() {
        return type;
    }

    public void setType(Value.Type type) {
        this.type = type;

        switch (type) {
            case EXECUTION:
                image.setDrawable(style.execution);
                break;
            case BOOL:
                image.setDrawable(style.bool);
                break;
            case INT:
                image.setDrawable(style.integer);
                break;
            case FLOAT:
                image.setDrawable(style.floating);
                break;
            case STRING:
                image.setDrawable(style.string);
                break;
        }
    }

    public int getConnection() {
        return connection;
    }

    public Value getValue() {

        Node parent = (Node) getParent().getParent();

        try {
            if (!(parent instanceof FunctionCallNode))
                parent.eval();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public boolean connect(Pin pin) {

        if (pin.getConnection() == getConnection() || pin.getParent() == getParent())
            return false;
        if (getType() != pin.getType()) {
            if (getType() == Value.Type.EXECUTION || pin.getType() == Value.Type.EXECUTION)
                return false;

            Vector2 pos;
            pos = getLocation().add(pin.getLocation());
            pos.x /= 2;
            pos.y /= 2;

            ConverterNode converter;
            if (getConnection() == input) {
                converter = new ConverterNode(pin.getType(), this.getType(), getSkin());
                converter.getPin("from").connect(pin);
                converter.getPin("to").connect(this);
            } else {
                converter = new ConverterNode(this.getType(), pin.getType(), getSkin());
                converter.getPin("from").connect(this);
                converter.getPin("to").connect(pin);
            }
            converter.setPosition(pos.x, pos.y);
            getStage().addActor(converter);
            return true;
        }

        if ((getType() == Value.Type.EXECUTION && getConnection() == output)
                || (getType() != Value.Type.EXECUTION && getConnection() == input)) {

            if (this.pin != null) pin.disconnect(this);

            this.pin = pin;
            this.pin.pins.add(this);

            WireConnector.base.addActor(new Wire(this, pin));

            return true;

        } else {
            return pin.connect(this);
        }
    }

    public void disconnect(Pin pin) {

        if ((getType() == Value.Type.EXECUTION && getConnection() == output)
                || (getType() != Value.Type.EXECUTION && getConnection() == input)) {

            this.pin = null;
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

    public Node getConnectionNode() {

        if (pin != null)
            return (Node) pin.getParent().getParent();

        return null;
    }

    public Pin getConnectionPin() {
        return pin;
    }

    public Vector2 getLocation() {
        return localToStageCoordinates(new Vector2(0, 0));
    }


    static public class PinStyle {
        public Drawable execution, bool, integer, floating, string;
        public BitmapFont font;
        public Color fontColor;

        public PinStyle() {
        }

        public PinStyle(Drawable execution, Drawable bool, Drawable integer, Drawable floating,
                        Drawable string, BitmapFont font, Color fontColor) {
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
