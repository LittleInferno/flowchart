package com.littleinferno.flowchart.node;

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
import com.littleinferno.flowchart.pin.Connector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class BaseNode extends CardView {


    public enum Align {
        LEFT, RIGHT, CENTER
    }

    private PointF delta = new PointF();

    NodeLayoutBinding layout;
    private PointF point;

    public BaseNode(final Context context) {
        super(context);
        layout = NodeLayoutBinding.inflate((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE), this, true);
        setElevation(8);
        bringToFront();
        setLayoutParams(selfLayoutParams());
    }

    private void init(Context context) {
    }

    public Connector addDataInputPin(final String name, final boolean isArray, final DataType... possibleConvert) {
        return buildPin(Connection.INPUT, name, isArray, possibleConvert);
    }

    public Connector addDataOutputPin(final String name, final boolean isArray, final DataType... possibleConvert) {
        return buildPin(Connection.OUTPUT, name, isArray, possibleConvert);
    }

    public Connector addExecutionInputPin(final String name) {
        return buildPin(Connection.INPUT, name, false, DataType.EXECUTION);
    }

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
                Optional.of(new HashSet<DataType>(Arrays.asList(possibleConverts))),
                DataType.UNIVERSAL);
    }

    public void removePin(final Connector pin) {
        layout.nodeLeft.removeView(pin);
        layout.nodeRight.removeView(pin);

    }

    public List<Connector> getPins() {

        List<Connector> pins = Stream.range(0, layout.nodeLeft.getChildCount())
                .map(layout.nodeLeft::getChildAt)
                .filter(value -> value instanceof Connector)
                .map(Connector.class::cast)
                .toList();

        pins.addAll(Stream.range(0, layout.nodeRight.getChildCount())
                .map(layout.nodeRight::getChildAt)
                .filter(value -> value instanceof Connector)
                .map(Connector.class::cast)
                .toList());

        return pins;
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
    }

    public PointF getDelta() {
        return delta;
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

    public static class ShadowBuilder extends View.DragShadowBuilder {

        private final PointF touchPoint;

        public ShadowBuilder(BaseNode node) {
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
}
