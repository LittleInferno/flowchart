package com.littleinferno.flowchart.pin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.littleinferno.flowchart.wire.Wire;
import com.littleinferno.flowchart.wire.WireConnector;

import java.util.ArrayList;
import java.util.Iterator;

public class Pin extends Image {

    public Data getData() {
        return data;
    }

    public Connection getConnection() {
        return connection;
    }

    public enum Connection {
        INPUT,
        OUTPUT
    }

    public enum Data {
        EXECUTION,
        BOOL,
        INT,
        FLOAT,
        STRING,
    }

    public Pin(Connection connection, Data data) {
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

    public boolean connect(Pin pin) {

        if (pin.getConnection().equals(getConnection()) || pin.getParent() == getParent())
            return false;

        if ((getData() == Data.EXECUTION && getConnection() == Connection.OUTPUT)
                || (getData() != Data.EXECUTION && getConnection() == Connection.INPUT)) {

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

        if ((getData() == Data.EXECUTION && getConnection() == Connection.OUTPUT)
                || (getData() != Data.EXECUTION && getConnection() == Connection.INPUT)) {

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

    @Override
    public void act(float delta) {
        super.act(delta);
    }


    static private TextureRegion getTexture(Data data) {

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

    private Pin pin;
    private ArrayList<Pin> pins;
    private Connection connection;
    private Data data;

    private static Texture texture = new Texture(Gdx.files.internal("pin.png"));
}
