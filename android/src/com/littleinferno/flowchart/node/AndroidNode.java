package com.littleinferno.flowchart.node;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.databinding.NodeLayoutBinding;
import com.littleinferno.flowchart.function.AndroidFunction;
import com.littleinferno.flowchart.pin.Connector;
import com.littleinferno.flowchart.plugin.AndroidPluginHandle;
import com.littleinferno.flowchart.scene.AndroidSceneLayout;

import java.util.Arrays;
import java.util.HashSet;

@SuppressLint("ViewConstructor")
public class AndroidNode extends CardView {

    public float getHeaderHeight() {
        return layout.header.getBottom();
    }

    public void setClosable(Integer closable) {
        layout.close.setVisibility(closable);
    }

    public enum Align {
        LEFT, RIGHT, CENTER
    }

    NodeLayoutBinding layout;
    private AndroidFunction function;
    private PointF point;
    private final AndroidPluginHandle.NodeHandle nodeHandle;

    public AndroidNode(final AndroidFunction function, final AndroidPluginHandle.NodeHandle nodeHandle) {
        super(function.getProject().getContext());
        layout = NodeLayoutBinding.inflate((LayoutInflater) function.getProject().getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE), this, true);

        this.function = function;
        this.nodeHandle = nodeHandle;

        setElevation(8);
        bringToFront();
        setLayoutParams(selfLayoutParams());

        setTitle(nodeHandle.getTitle());

        this.nodeHandle.getInit().call(this);
        this.nodeHandle.getAttribute("closable")
                .map(Boolean.class::cast)
                .map(o -> o ? VISIBLE : INVISIBLE)
                .ifPresent(this::setClosable);

        layout.close.setOnClickListener(v -> function.getNodeManager().removeNode(this));
    }

    private void init(Context context) {
    }

    @SuppressWarnings("unused")
    public Connector addDataInputPin(final String name, final boolean isArray, final DataType... possibleConvert) {
        return buildPin(Connection.INPUT, name, isArray, possibleConvert);
    }

    @SuppressWarnings("unused")
    public Connector addDataOutputPin(final String name, final boolean isArray, final DataType... possibleConvert) {
        return buildPin(Connection.OUTPUT, name, isArray, possibleConvert);
    }

    @SuppressWarnings("unused")
    public Connector addExecutionInputPin(final String name) {
        return buildPin(Connection.INPUT, name, false, DataType.EXECUTION);
    }

    @SuppressWarnings("unused")
    public Connector addExecutionOutputPin(final String name) {
        return buildPin(Connection.OUTPUT, name, false, DataType.EXECUTION);
    }

    Connector buildPin(final Connection connection, final String name, final boolean isArray, final DataType type) {
        return new Connector(this, createLayoutParams(),
                connection, name, isArray, Optional.empty(), type);
    }

    Connector buildPin(final Connection connection, final String name, final boolean isArray, final DataType... possibleConverts) {
        if (possibleConverts.length == 1)
            return buildPin(connection, name, isArray, possibleConverts[0]);

        return new Connector(this, createLayoutParams(),
                connection, name, isArray,
                Optional.of(new HashSet<>(Arrays.asList(possibleConverts))),
                DataType.UNIVERSAL);
    }

    @SuppressWarnings("unused")
    public void removePin(final Connector pin) {
        pin.disconnectAll();
        layout.nodeLeft.removeView(pin);
        layout.nodeRight.removeView(pin);
        invalidate();
    }

    @SuppressWarnings("unused")
    public void removePin(final String pin) {
        Stream.range(0, layout.nodeLeft.getChildCount())
                .map(layout.nodeLeft::getChildAt)
                .filter(value -> value instanceof Connector)
                .map(Connector.class::cast)
                .filter(connector -> connector.getName().equals(pin))
                .findFirst().ifPresent(p -> layout.nodeLeft.removeView(p));

        Stream.range(0, layout.nodeRight.getChildCount())
                .map(layout.nodeRight::getChildAt)
                .filter(value -> value instanceof Connector)
                .map(Connector.class::cast)
                .filter(connector -> connector.getName().equals(pin))
                .findFirst().ifPresent(p -> layout.nodeRight.removeView(p));
    }

    public Connector[] getPins() {
        return Stream.concat(
                Stream.range(0, layout.nodeLeft.getChildCount())
                        .map(layout.nodeLeft::getChildAt)
                        .filter(value -> value instanceof Connector)
                        .map(Connector.class::cast),
                Stream.range(0, layout.nodeRight.getChildCount())
                        .map(layout.nodeRight::getChildAt)
                        .filter(value -> value instanceof Connector)
                        .map(Connector.class::cast)).toArray(Connector[]::new);
    }

    @SuppressWarnings("unused")
    public Connector getPin(String name) {
        return Stream.of(getPins())
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .get();
    }

    LinearLayout.LayoutParams createLayoutParams() {
        return new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    RelativeLayout.LayoutParams selfLayoutParams() {
        return new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    public void addView(Align align, View view) {
        view.setLayoutParams(createLayoutParams());
        switch (align) {
            case LEFT:
                layout.nodeLeft.addView(view);
                break;
            case RIGHT:
                layout.nodeRight.addView(view);
                break;
            case CENTER:
                layout.container.addView(view);
                break;
        }
        invalidate();
    }

    public void drag() {
        ClipData data = ClipData.newPlainText("", "");
        ShadowBuilder shadowBuilder = new ShadowBuilder(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            startDragAndDrop(data, shadowBuilder, this, 0);
        else
            startDrag(data, shadowBuilder, this, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            setPoint(event.getX(), event.getY());
            drag();
            setVisibility(View.INVISIBLE);
        }
        return true;
    }

    public void setPoint(float x, float y) {
        point = new PointF(x, y);
    }

    public PointF getPoint() {
        return point;
    }

    public void setTitle(final String title) {
        layout.nodeTitle.setText(title);
    }

    public String getText() {
        return layout.nodeTitle.getText().toString();
    }

    public AndroidFunction getFunction() {
        return function;
    }

    public AndroidSceneLayout getScene() {
        return (AndroidSceneLayout) getParent();
    }

    public AndroidPluginHandle.NodeHandle getNodeHandle() {
        return nodeHandle;
    }

    public SimpleObject getSaveInfo() {
        return new SimpleObject(getNodeHandle().getName(), getX(), getY());
    }

    private static class ShadowBuilder extends View.DragShadowBuilder {

        private final PointF touchPoint;

        ShadowBuilder(AndroidNode node) {
            super(node);
            this.touchPoint = node.getPoint();
        }

        @Override
        public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {
            int width;
            int height;

            width = (int) (getView().getWidth() * getView().getScaleX());
            height = (int) (getView().getHeight() * getView().getScaleY());

            outShadowSize.set(width, height);

            touchPoint.x *= getView().getScaleX();
            touchPoint.y *= getView().getScaleY();

            outShadowTouchPoint.set((int) touchPoint.x, (int) touchPoint.y);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            canvas.scale(getView().getScaleX(), getView().getScaleY());
            getView().draw(canvas);
        }
    }

    public static class SimpleObject {
        final String name;
        final float x;
        final float y;

        public SimpleObject(String name, float x, float y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }
    }
}
