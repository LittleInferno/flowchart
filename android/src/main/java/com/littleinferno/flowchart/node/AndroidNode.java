package com.littleinferno.flowchart.node;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Build;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.littleinferno.flowchart.databinding.NodeLayoutBinding;
import com.littleinferno.flowchart.function.Function;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.plugin.PluginHandle;
import com.littleinferno.flowchart.plugin.bridge.ViewName;
import com.littleinferno.flowchart.scene.AndroidSceneLayout;
import com.littleinferno.flowchart.util.Connection;
import com.littleinferno.flowchart.util.DataType;

import org.mozilla.javascript.NativeArray;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

@SuppressLint("ViewConstructor")
public class AndroidNode extends CardView {

    HashMap<String, String> nodeAttributes = new HashMap<>();

    private UUID id;
    private String[] attributes;

    public float getHeaderHeight() {
        return layout.header.getBottom();
    }

    public void setClosable(Integer closable) {
        layout.close.setVisibility(closable);
    }

    public UUID getNodeId() {
        return id;
    }

    public void setNodeId(UUID id) {
        this.id = id;
    }

    public enum Align {
        LEFT, RIGHT, CENTER
    }

    NodeLayoutBinding layout;
    private Function function;
    private PointF point;
    private final PluginHandle.NodeHandle nodeHandle;

    public AndroidNode(final Function function, final PluginHandle.NodeHandle nodeHandle) {
        super(function.getProject().getContext());
        layout = NodeLayoutBinding.inflate((LayoutInflater) function.getProject().getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE), this, true);

        this.function = function;
        this.nodeHandle = nodeHandle;

        setElevation(8);
        bringToFront();
        setLayoutParams(selfLayoutParams());

        setTitle(nodeHandle.getTitle());

        this.nodeHandle.getInit().call((AndroidNode) this);
        this.nodeHandle.getAttribute("closable")
                .map(Boolean.class::cast)
                .map(o -> o ? VISIBLE : INVISIBLE)
                .ifPresent(this::setClosable);

        layout.close.setOnClickListener(v -> function.getNodeManager().removeNode(this));

        id = UUID.randomUUID();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    @SuppressWarnings("unused")
    public Pin addDataInputPin(final String name, final boolean isArray, final DataType... possibleConvert) {
        return buildPin(Connection.INPUT, name, isArray, possibleConvert);
    }

    @SuppressWarnings("unused")
    public Pin addDataOutputPin(final String name, final boolean isArray, final DataType... possibleConvert) {
        return buildPin(Connection.OUTPUT, name, isArray, possibleConvert);
    }

    @SuppressWarnings("unused")
    public Pin addExecutionInputPin(final String name) {
        return buildPin(Connection.INPUT, name, false, DataType.EXECUTION);
    }

    @SuppressWarnings("unused")
    public Pin addExecutionOutputPin(final String name) {
        return buildPin(Connection.OUTPUT, name, false, DataType.EXECUTION);
    }

    Pin buildPin(final Connection connection, final String name, final boolean isArray, final DataType type) {
        return new Pin(this, createLayoutParams(),
                connection, name, isArray, Optional.empty(), type);
    }

    Pin buildPin(final Connection connection, final String name, final boolean isArray, final DataType... possibleConverts) {
        if (possibleConverts.length == 1)
            return buildPin(connection, name, isArray, possibleConverts[0]);

        return new Pin(this, createLayoutParams(),
                connection, name, isArray,
                Optional.of(new HashSet<>(Arrays.asList(possibleConverts))),
                DataType.UNIVERSAL);
    }

    @SuppressWarnings("unused")
    public void removePin(final Pin pin) {
        pin.disconnectAll();
        layout.nodeLeft.removeView(pin);
        layout.nodeRight.removeView(pin);
        invalidate();
    }

    @SuppressWarnings("unused")
    public void removePin(final String pin) {
        Stream.range(0, layout.nodeLeft.getChildCount())
                .map(layout.nodeLeft::getChildAt)
                .filter(value -> value instanceof Pin)
                .map(Pin.class::cast)
                .filter(connector -> connector.getName().equals(pin))
                .findFirst().ifPresent(p -> layout.nodeLeft.removeView(p));

        Stream.range(0, layout.nodeRight.getChildCount())
                .map(layout.nodeRight::getChildAt)
                .filter(value -> value instanceof Pin)
                .map(Pin.class::cast)
                .filter(connector -> connector.getName().equals(pin))
                .findFirst().ifPresent(p -> layout.nodeRight.removeView(p));
    }

    public Pin[] getPins() {
        return Stream.concat(
                Stream.range(0, layout.nodeLeft.getChildCount())
                        .map(layout.nodeLeft::getChildAt)
                        .filter(value -> value instanceof Pin)
                        .map(Pin.class::cast),
                Stream.range(0, layout.nodeRight.getChildCount())
                        .map(layout.nodeRight::getChildAt)
                        .filter(value -> value instanceof Pin)
                        .map(Pin.class::cast)).toArray(Pin[]::new);
    }

    @SuppressWarnings("unused")
    public Pin getPin(String name) {
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

    public View getView(String name) {

        View viewName = null;

        for (int i = 0; i < layout.nodeLeft.getChildCount(); i++) {
            View view = layout.nodeLeft.getChildAt(i);
            if (view instanceof ViewName)
                if (((ViewName) view).getName().equals(name)) {
                    viewName = view;
                }
        }

        for (int i = 0; i < layout.nodeRight.getChildCount(); i++) {
            View view = layout.nodeRight.getChildAt(i);
            if (view instanceof ViewName)
                if (((ViewName) view).getName().equals(name)) {
                    viewName = view;
                }
        }

        for (int i = 0; i < layout.container.getChildCount(); i++) {
            View view = layout.container.getChildAt(i);
            if (view instanceof ViewName)
                if (((ViewName) view).getName().equals(name)) {
                    viewName = view;
                }
        }
        return viewName;
    }

    @SuppressWarnings("unused")
    public void putAttribute(String key, String value) {
        nodeAttributes.put(key, value);
    }

    @SuppressWarnings("unused")
    public String getAttribute(String key) {
        return nodeAttributes.get(key);
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

    public Function getFunction() {
        return function;
    }

    public AndroidSceneLayout getScene() {
        return (AndroidSceneLayout) getParent();
    }

    public PluginHandle.NodeHandle getNodeHandle() {
        return nodeHandle;
    }

    public SimpleObject getSaveInfo() {

        if (nodeHandle.containsAttribute("save")) {
            Object save = nodeHandle.getAttribute("save").get();
            NativeArray call = (NativeArray) nodeHandle.getPluginHandle()
                    .createScriptFun((org.mozilla.javascript.Function) save)
                    .call(this);

            String[] attributes = new String[call.size()];

            for (int i = 0; i < call.size(); i++) attributes[i] = (String) call.get(i);

            return new SimpleObject(getNodeHandle().getName(), getX(), getY(), id.toString(), attributes);
        }

        return new SimpleObject(getNodeHandle().getName(), getX(), getY(), id.toString());
    }

    public void setLoadAttributes(String[] attributes) {
        this.attributes = attributes;
    }

    @SuppressWarnings("unused")
    public void removeDataPin() {
        Stream.of(getPins()).filter(pin -> pin.getType() != DataType.EXECUTION).forEach(this::removePin);
    }

    public void load() {
        nodeHandle.getAttribute("load")
                .ifPresent(o ->
                        nodeHandle
                                .getPluginHandle()
                                .createScriptFun((org.mozilla.javascript.Function) o)
                                .call(this, attributes));
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
        final String id;
        final String[] attributes;

        SimpleObject(String name, float x, float y, String id, String... attributes) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.id = id;
            this.attributes = attributes;
        }
    }
}
