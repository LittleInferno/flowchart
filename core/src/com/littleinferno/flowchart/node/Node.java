package com.littleinferno.flowchart.node;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.littleinferno.flowchart.pin.Pin;

public class Node extends Table {

    public Node(Vector2 position, CharSequence header) {
        this.sprite = new Sprite(new Texture(Gdx.files.internal("node.png")));

        this.setPosition(position.x, position.y);
        this.setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        this.setTouchable(Touchable.enabled);

        sprite.setPosition(position.x, position.y);

        Label h = new Label(header, skin);
        h.setAlignment(Align.center);
        add(h).expandX().fillX().top();
        row();

        pins = new Table();
        add(pins).expandX().fillX();

        h.pack();
        headerHeight = h.getHeight();

        left = new Table();
        left.setName("left");
        left.left().top();
        pins.add(left).expand().fill();

        right = new Table();
        right.setName("right");
        right.right().top();
        pins.add(right).expand().fill();

        top();

        addListener(new InputListener() {

            private float mouseOfsetX;
            private float mouseOfsetY;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                mouseOfsetX = x;
                mouseOfsetY = y;

                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {

                float nodeX = getX(), nodeY = getY();

                float amountX = x - mouseOfsetX, amountY = y - mouseOfsetY;

                setPosition(((int) ((nodeX + amountX) / 10) * 10), ((int) ((nodeY + amountY) / 10) * 10));
            }
        });
    }

    public void addDataInputPin(final Pin.Data type, final String name) {
        left.add(new Pin(Pin.Connection.INPUT, type)).padLeft(10).width(16);
        left.add(new Label(name, skin)).expandX();
        left.row();
        updateSize();
    }

    public void addDataOutputPin(final Pin.Data type, final String name) {
        right.add(new Label(name, skin)).expandX();
        right.add(new Pin(Pin.Connection.OUTPUT, type)).padRight(10).width(16);
        right.row();
        updateSize();
    }

    public void addExecutionInputPin(final String name) {
        left.add(new Pin(Pin.Connection.INPUT, Pin.Data.EXECUTION)).padLeft(10).width(16);
        left.add(new Label(name, skin)).expandX();
        left.row();
        updateSize();
    }

    public void addExecutionOutputPin(final String name) {
        right.add(new Label(name, skin)).expandX();
        right.add(new Pin(Pin.Connection.OUTPUT, Pin.Data.EXECUTION)).padRight(20).width(16);
        right.row();
        updateSize();
    }

    public void removePin(final String name) {
        //*
        //TODO
        // */
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        sprite.setPosition(getX(), getY());

    }

    protected void updateSize() {

        pins.pack();

        if (pins.getHeight() + headerHeight > getHeight()) {
            setHeight(pins.getHeight() + headerHeight);
            sprite.setSize(sprite.getWidth(), pins.getHeight() + headerHeight);
        }
    }

    protected Table left;
    protected Table right;
    protected Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

    private Sprite sprite;

    private float headerHeight;
    private Table pins;

}
