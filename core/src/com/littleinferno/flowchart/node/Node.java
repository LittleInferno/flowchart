package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.CodeGen;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.ui.Main;

public abstract class Node extends VisWindow implements CodeGen {

    protected final VerticalGroup left;
    protected final VerticalGroup right;
    final NodeStyle style;
    Skin skin;

    public Node(final String name, final boolean closable) {
        this(name, closable, Main.skin);
    }

    public Node(final String name, final boolean closable, Skin skin) {
        super(name);

        style = skin.get(NodeStyle.class);
        this.skin = skin;
        setWidth(200);

        if (closable) {
            addCloseButton();
        }

        VisTable container = new VisTable();
        VisTable main = new VisTable();
        main.addSeparator();

        left = new VerticalGroup();
        left.top().left().fill();
        left.space(10);
        container.add(left).grow().width(100);

        container.addSeparator(true);

        right = new VerticalGroup();
        right.top().right().fill();
        right.space(10);
        container.add(right).grow().width(100);

        main.add(container).grow();

        add(main).grow();
        top();

//        addListener(new InputListener() {
//
//            private float mouseOffsetX;
//            private float mouseOffsetY;
//
//            @Override
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//
//                mouseOffsetX = x;
//                mouseOffsetY = y;
//                return true;
//            }
//
//            @Override
//            public void touchDragged(InputEvent event, float x, float y, int pointer) {
//
//                float nodeX = getX(), nodeY = getY();
//
//                float amountX = x - mouseOffsetX, amountY = y - mouseOffsetY;
//
//                setPosition(((int) ((nodeX + amountX) / 10) * 10), ((int) ((nodeY + amountY) / 10) * 10));
//            }
//
//            @Override
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//            }
//        });
    }

    public void destroy() {
        getParent().removeActor(Node.this);
    }


    public Pin addDataInputPin(final String name, DataType... possibleConvert) {
        Pin pin = new Pin(name, Connection.INPUT, possibleConvert);
        left.addActor(pin);
        pack();
        return pin;
    }

    public Pin addDataInputPin(final DataType type, final String name) {
        Pin pin = new Pin(name, type, Connection.INPUT);
        left.addActor(pin);//.expandX().fillX().padLeft(10).padBottom(10);
        // left.row();
        pack();
        return pin;
    }

    public Pin addDataOutputPin(final String name, DataType... possibleConvert) {
        Pin pin = new Pin(name, Connection.OUTPUT, possibleConvert);
        right.addActor(pin);
        pack();
        return pin;
    }

    public Pin addDataOutputPin(final DataType type, final String name) {
        Pin pin = new Pin(name, type, Connection.OUTPUT);
        right.addActor(pin);//.expandX().fillX().padRight(10).padBottom(10);
        //right.row();
        pack();
        return pin;
    }


    public Pin addExecutionInputPin() {
        return addExecutionInputPin("exec in");
    }

    public Pin addExecutionInputPin(final String name) {
        Pin pin = new Pin(name, DataType.EXECUTION, Connection.INPUT);
        left.addActor(pin);//.expandX().fillX().padLeft(10).padBottom(10);
        //left.row();
        pack();
        return pin;
    }

    public Pin addExecutionOutputPin() {
        return addExecutionOutputPin("exec out");
    }

    public Pin addExecutionOutputPin(final String name) {
        Pin pin = new Pin(name, DataType.EXECUTION, Connection.OUTPUT);
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
        getTitleLabel().setText(text);
    }

    @Override
    public void close() {
        super.close();
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
