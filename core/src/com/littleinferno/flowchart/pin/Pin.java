package com.littleinferno.flowchart.pin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.littleinferno.flowchart.node.ConverterNode;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.value.Value;
import com.littleinferno.flowchart.wire.Wire;
import com.littleinferno.flowchart.wire.WireConnector;

import java.util.ArrayList;
import java.util.Iterator;


public class Pin extends Image {

    public enum Connection {
        INPUT,
        OUTPUT
    }

    public Pin(Connection connection, Value.Type data) {
        super(getTexture(data));

        this.pins = new ArrayList<Pin>();
        this.connection = connection;

        this.data = data;
        addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                WireConnector.add(Pin.this);
                return true;
            }

        });
    }

    public Value.Type getData() {
        return data;
    }

    public void setData(Value.Type data) {
        this.data = data;
        setDrawable(new TextureRegionDrawable(getTexture(data)));
    }


    public Connection getConnection() {
        return connection;
    }

    public boolean connect(Pin pin) {

        if (pin.getConnection() == getConnection() || pin.getParent() == getParent())
            return false;
        if (getData() != pin.getData()) {
            if (getData() == Value.Type.EXECUTION || pin.getData() == Value.Type.EXECUTION)
                return false;

            Vector2 pos = new Vector2();

            pos = getLocation().add(pin.getLocation());
            pos.x /= 2;
            pos.y /= 2;

            ConverterNode converter;
            if (getConnection() == Connection.INPUT) {
                converter = new ConverterNode(pin.getData(), this.getData());
                converter.getItem("from").getPin().connect(pin);
                converter.getItem("to").getPin().connect(this);
            } else {
                converter = new ConverterNode(this.getData(), pin.getData());
                converter.getItem("from").getPin().connect(this);
                converter.getItem("to").getPin().connect(pin);
            }
            converter.setPosition(pos.x, pos.y);
            getStage().addActor(converter);
            return true;
        }

        if ((getData() == Value.Type.EXECUTION && getConnection() == Connection.OUTPUT)
                || (getData() != Value.Type.EXECUTION && getConnection() == Connection.INPUT)) {

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

        if ((getData() == Value.Type.EXECUTION && getConnection() == Connection.OUTPUT)
                || (getData() != Value.Type.EXECUTION && getConnection() == Connection.INPUT)) {

            this.pin = null;
        } else {

            pin.disconnect(this);
            Iterator<Pin> i = pins.iterator();
            while (i.hasNext()) {
                if (i.next() == pin) {
                    i.remove();
                }
            }

        }
    }

    static private TextureRegion getTexture(Value.Type data) {

        switch (data) {
            case EXECUTION:
                return new TextureRegion(texture, 00, 0, 16, 16);
            case BOOL:
                return new TextureRegion(texture, 16, 0, 16, 16);
            case INT:
                return new TextureRegion(texture, 32, 0, 16, 16);
            case FLOAT:
                return new TextureRegion(texture, 48, 0, 16, 16);
            case STRING:
                return new TextureRegion(texture, 64, 0, 16, 16);
        }

        return null;
    }

    public Vector2 getLocation() {
        return localToStageCoordinates(new Vector2(0, 0));
    }

    public Node getConnectionNode() {

        if (pin != null)
            return (Node) pin.getParent().getParent().getParent();

        return null;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    private Pin pin;
    private ArrayList<Pin> pins;
    private final Connection connection;
    private Value.Type data;
    private Value value;
    private static Texture texture = new Texture(Gdx.files.internal("pin.png"));
}
