package com.littleinferno.flowchart.node;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g3d.particles.batches.BufferedParticleBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.value.Value;

public class Node extends Table {

    private final Label title;

    public static class Item extends Table {
        Label label;
        Pin pin;

        Item(String name, Pin.Connection connection, Value.Type value, Skin skin) {
            super.setName(name);

            this.label = new Label(name, skin);
            this.label.setEllipsis(true);
            this.pin = new Pin(connection, value);

            if (connection == Pin.Connection.INPUT) {
                add(pin).padLeft(10).size(16);
                add(label).expandX().fillX().minWidth(0);
            } else {
                add(label).expandX().fillX().minWidth(0);
                add(pin).padRight(10).size(16);
            }
        }

        public Pin getPin() {
            return pin;
        }

        @Override
        public void setName(String name) {
            super.setName(name);
            label.setText(name);
        }
    }

    public Node(final String name, final boolean closable) {

        setWidth(200);
        NinePatch patch = new NinePatch(new Texture(Gdx.files.internal("VarTable.png")), 1, 1, 1, 1);
        setBackground(new NinePatchDrawable(patch));

        this.setTouchable(Touchable.enabled);


        title = new Label(name, skin);
        title.setName("title");
        title.setAlignment(Align.center);

        if (closable) {
            Table container = new Table();

            container.add(title).expandX().fillX();

            Button close = new Button(skin);
            container.add(close).width(30);

            add(container).expandX().fillX().top().colspan(2).row();

            close.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Node.this.getParent().removeActor(Node.this);
                }
            });

        } else {
            add(title).expandX().fillX().top().colspan(2).row();
        }

        title.pack();
        headerHeight = title.getHeight();

        row().width(100);

        left = new Table();
        left.left().top();
        add(left).expand().fill();

        right = new Table();
        right.right().top();
        add(right).expand().fill();

        top();

        addListener(new InputListener() {

            private float mouseOffsetX;
            private float mouseOffsetY;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                mouseOffsetX = x;
                mouseOffsetY = y;

                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {

                float nodeX = getX(), nodeY = getY();

                float amountX = x - mouseOffsetX, amountY = y - mouseOffsetY;

                setPosition(((int) ((nodeX + amountX) / 10) * 10), ((int) ((nodeY + amountY) / 10) * 10));
            }
        });
    }

    public void addDataInputPin(final Value.Type type, final String name) {
        left.add(new Item(name, Pin.Connection.INPUT, type, skin)).expandX().fillX().height(16);

        left.row();
        updateSize();
    }

    public void addDataOutputPin(final Value.Type type, final String name) {
        right.add(new Item(name, Pin.Connection.OUTPUT, type, skin)).expandX().fillX().height(16);
        right.row();
        updateSize();
    }

    void addExecutionInputPin(final String name) {
        left.add(new Item(name, Pin.Connection.INPUT, Value.Type.EXECUTION, skin)).expandX().fillX().height(16);
        left.row();
        updateSize();
    }

    void addExecutionOutputPin(final String name) {
        right.add(new Item(name, Pin.Connection.OUTPUT, Value.Type.EXECUTION, skin)).expandX().fillX().height(16);
        right.row();
        updateSize();
    }

    public void removePin(final String name) {
        left.removeActor(left.findActor(name));
        right.removeActor(right.findActor(name));
    }

    public Item getItem(String name) {
        return (Item) findActor(name);
    }

    public Array<Item> getInputItems() {
        Array<Actor> children = left.getChildren();

        Array<Item> result = new Array<Item>(children.size);

        for (Actor i : children) result.add((Item) i);

        return result;
    }

    public Array<Item> getOutputItems() {
        Array<Actor> children = right.getChildren();

        Array<Item> result = new Array<Item>(children.size);

        for (Actor i : children) result.add((Item) i);

        return result;
    }

    public void setTitle(String text) {
        title.setText(text);
    }

    void execute() throws Exception {
        throw new Exception("can`t execute");
    }

    Value evaluate() throws Exception {
        throw new Exception("can`t evaluate");
    }

    private void updateSize() {

        left.pack();
        float height = left.getHeight();
        right().pack();
        height = Math.max(height, right.getHeight());
        if (height > getHeight()) {
            setHeight(height);
        }
    }

    Table left;
    Table right;
    protected Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

    private float headerHeight;

}
