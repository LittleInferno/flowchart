package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.value.Value;

public class Node extends Table {

    private final Label title;
    final Table left;
    final Table right;
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
                    Node.this.getParent().removeActor(Node.this);
                }
            });

        } else {
            add(title).expandX().fillX().minWidth(0).colspan(2).row();
        }

        row().width(100);

        left = new Table();
       // left.top();
        add(left).expand().fill();

        right = new Table();
       // right.top();
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

    public void addDataInputPin(final Value.Type type, final String name) {
        left.add(new Pin(name, type, Pin.input, getSkin())).expandX().fillX().padLeft(10).padBottom(10);
        ;
        left.row();
        pack();
    }

    public void addDataOutputPin(final Value.Type type, final String name) {
        right.add(new Pin(name, type, Pin.output, getSkin())).expandX().fillX().padRight(10).padBottom(10);
        ;
        right.row();
        pack();
    }


    public void addExecutionInputPin() {
        addExecutionInputPin("exec in");
    }

    public void addExecutionInputPin(final String name) {
        left.add(new Pin(name, Value.Type.EXECUTION, Pin.input, getSkin())).expandX().fillX().padLeft(10).padBottom(10);
        ;
        left.row();
        pack();
    }

    public void addExecutionOutputPin() {
        addExecutionOutputPin("exec out");
    }

    public void addExecutionOutputPin(final String name) {
        right.add(new Pin(name, Value.Type.EXECUTION, Pin.output, getSkin())).expandX().fillX().padRight(10).padBottom(10);
        right.row();
        pack();
    }

    public void removePin(final String name) {
        left.removeActor(left.findActor(name));
        right.removeActor(right.findActor(name));
    }

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

    @Deprecated
    public void execute() throws Exception {
        throw new Exception("can`t execute");
    }

    @Deprecated
    public void executeNext() {
        Node next = getPin("exec out").getConnectionNode();

        if (next != null) {
            try {
                next.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Deprecated
    public void eval() throws Exception {
        throw new Exception("can`t eval");
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
