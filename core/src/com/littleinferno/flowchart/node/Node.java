package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.CodeGen;
import com.littleinferno.flowchart.pin.Pin;

public abstract class Node extends Table implements CodeGen {

    private final Label title;
    protected final VerticalGroup left;
    protected final VerticalGroup right;
    final NodeStyle style;


    public Node(final String name, final boolean closable, Skin skin) {
        super(skin);
        style = skin.get(NodeStyle.class);

        setWidth(200);
        setBackground(style.normal);

        this.setTouchable(Touchable.enabled);

        title = new Label(name, getSkin());
        title.setAlignment(Align.center);
        title.setEllipsis(true);

        if (closable) {
            Table container = new Table();

            container.add(title).expand().fill().minWidth(0);

            Button close = new Button(getSkin());
            container.add(close).width(30);

            add(container).expandX().fillX().colspan(2).row();

            close.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    destroy();
                }
            });

        } else {
            add(title).expandX().fillX().minWidth(0).colspan(2).row();
        }

        row().width(100);

        left = new VerticalGroup();
        left.top().left();
        left.space(10);
        add(left).expand().fill();

        right = new VerticalGroup();
        right.top().right();
        right.space(10);
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

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            }
        });
    }

    public void destroy() {
        getParent().removeActor(Node.this);
    }

    public Pin addDataInputPin(final DataType type, final String name) {
        Pin pin = new Pin(name, type, Connection.INPUT, getSkin());
        left.addActor(pin);//.expandX().fillX().padLeft(10).padBottom(10);
       // left.row();
        pack();
        return pin;
    }

    public Pin addDataOutputPin(final DataType type, final String name) {
        Pin pin = new Pin(name, type, Connection.OUTPUT, getSkin());
        right.addActor(pin);//.expandX().fillX().padRight(10).padBottom(10);
        //right.row();
        pack();
        return pin;
    }


    public Pin addExecutionInputPin() {
        return addExecutionInputPin("exec in");
    }

    public Pin addExecutionInputPin(final String name) {
        Pin pin = new Pin(name, DataType.EXECUTION, Connection.INPUT, getSkin());
        left.addActor(pin);//.expandX().fillX().padLeft(10).padBottom(10);
        //left.row();
        pack();
        return pin;
    }

    public Pin addExecutionOutputPin() {
        return addExecutionOutputPin("exec out");
    }

    public Pin addExecutionOutputPin(final String name) {
        Pin pin = new Pin(name, DataType.EXECUTION, Connection.OUTPUT, getSkin());
        right.addActor(pin);//.expandX().fillX().padRight(10).padBottom(10);
        //right.row();
        pack();
        return pin;
    }

    public void removePin(final String name) {
        left.removeActor(left.findActor(name));
        right.removeActor(right.findActor(name));
    }

    public void removePin(final Pin pin) {
        left.removeActor(pin);
        right.removeActor(pin);
    }

    @Deprecated
    public Pin getPin(String name) {
        return (Pin) findActor(name);
    }

    public Array<Pin> getInput() {
        Array<Actor> children = left.getChildren();

        Array<Pin> result = new Array<Pin>(children.size);

        for (Actor i : children) result.add((Pin) i);

        return result;
    }

    public Array<Pin> getOutput() {
        Array<Actor> children = right.getChildren();

        Array<Pin> result = new Array<Pin>(children.size);

        for (Actor i : children) result.add((Pin) i);

        return result;
    }

    public void setTitle(String text) {
        title.setText(text);
    }

    static public class NodeStyle {

        public Drawable normal, select;

        public NodeStyle() {
        }

        public NodeStyle(Drawable normal, Drawable select) {
            this.normal = normal;
            this.select = select;

        }
    }
}
