package com.littleinferno.flowchart.node;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.value.Value;

public class Node extends Table {

    public Node(Vector2 position, CharSequence header) {

        setWidth(200);
        NinePatch patch = new NinePatch(new Texture(Gdx.files.internal("VarTable.png")), 1, 1, 1, 1);
        setBackground(new NinePatchDrawable(patch));

        this.setPosition(position.x, position.y);
        this.setTouchable(Touchable.enabled);

        Label h = new Label(header, skin);
        h.setName("title");
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

    void addDataInputPin(final Value.Type type, final String name) {
        Pin pin = new Pin(Pin.Connection.INPUT, type);
        pin.setName(name);
        left.add(pin).padLeft(10).width(16);
        left.add(new Label(name, skin)).expandX();
        left.row();
        updateSize();
    }

    void addDataOutputPin(final Value.Type type, final String name) {
        Pin pin = new Pin(Pin.Connection.OUTPUT, type);
        pin.setName(name);
        right.add(new Label(name, skin)).expandX();
        right.add(pin).padRight(10).width(16);
        right.row();
        updateSize();
    }

    void addExecutionInputPin(final String name) {
        Pin pin = new Pin(Pin.Connection.INPUT, Value.Type.EXECUTION);
        pin.setName(name);
        left.add(pin).padLeft(10).width(16);
        left.add(new Label(name, skin)).expandX();
        left.row();
        updateSize();
    }

    void addExecutionOutputPin(final String name) {
        Pin pin = new Pin(Pin.Connection.OUTPUT, Value.Type.EXECUTION);
        pin.setName(name);
        right.add(new Label(name, skin)).expandX();
        right.add(pin).padRight(20).width(16);
        right.row();
        updateSize();
    }

    public void removePin(final String name) {
        left.removeActor(left.findActor(name));
        right.removeActor(right.findActor(name));
    }

    public Pin get(String name) {
        return (Pin) findActor(name);
    }

    public void setTitle(String title) {
        ((Label) findActor("title")).setText(title);
    }

    void execute() throws Exception {
        throw new Exception("can`t execute");
    }

    Value evaluate() throws Exception {
        throw new Exception("can`t evaluate");
    }

    protected void updateSize() {

        pins.pack();
        if (pins.getHeight() + headerHeight > getHeight()) {
            setHeight(pins.getHeight() + headerHeight);
        }
    }

    Table left;
    Table right;
    protected Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

    private float headerHeight;
    private Table pins;

}
