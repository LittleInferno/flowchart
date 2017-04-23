package com.littleinferno.flowchart.scene;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.RelativeLayout;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.project.FlowchartProject;
import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.node.AndroidNodeManager;
import com.littleinferno.flowchart.node.BaseNode;
import com.littleinferno.flowchart.pin.Connector;

import java.util.ArrayList;

public class AndroidSceneLayout extends RelativeLayout {
    private final Paint paint;
    private final GestureDetector gestureDetector;
    private final ScaleGestureDetector scaleDetector;
    private FlowchartProject project;
    private float scaleFactor = 1;
    private PointF delta;
    private final String sceneType;
    private final AndroidNodeManager nodeManager;

    public FlowchartProject getProject() {
        return project;
    }

    public String getSceneType() {
        return sceneType;
    }

    private static class Wire {
        final Connector begin, end;

        private Wire(Connector begin, Connector end) {
            this.begin = begin;
            this.end = end;
        }
    }

    ArrayList<Wire> wires = new ArrayList<>();

    public AndroidNodeManager getNodeManager() {
        return nodeManager;
    }

    public AndroidSceneLayout(Context context, String sceneType) {
        super(context);
        paint = new Paint();
        gestureDetector = new GestureDetector(context, new GestureListener());
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        setWillNotDraw(false);
        delta = new PointF();
        project = FlowchartProject.load();
        nodeManager = new AndroidNodeManager(this);
        this.sceneType = sceneType;
    }

    public AndroidSceneLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        gestureDetector = new GestureDetector(context, new GestureListener());
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        setWillNotDraw(false);
        delta = new PointF();
        project = FlowchartProject.load();
        nodeManager = new AndroidNodeManager(this);
        sceneType = "main";
    }

    @Override
    public boolean dispatchDragEvent(DragEvent event) {
        return super.dispatchDragEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //gestureDetector.onTouchEvent(event);
        scaleDetector.onTouchEvent(event);
        return true;
    }

    private void showMenu(float rawX, float rawY) {

        PopupMenu popupMenu = new PopupMenu(getContext(), this);
        popupMenu.inflate(R.menu.nodes);


    }

    public void addWire(Connector connector, Connector pin) {
        wires.add(new Wire(connector, pin));
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            scrollBy((int) distanceX, (int) distanceY);
            return true;
        }
    }

    public RelativeLayout.LayoutParams createLayoutParams() {
        return new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    public float getScaleFactor() {
        return scaleFactor;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();

            scaleFactor = Math.max(0.5f, Math.min(scaleFactor, 1.5f));

            Stream.range(0, getChildCount())
                    .map(AndroidSceneLayout.this::getChildAt)
                    .peek(v -> v.setPivotX(0))
                    .peek(v -> v.setPivotY(0))
                    .peek(v -> v.setScaleX(scaleFactor))
                    .forEach(v -> v.setScaleY(scaleFactor));

            invalidate();
            return true;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {

        //  if (update)
        {
            canvas.translate(delta.x, delta.y);
        }
//        canvas.scale(scaleFactor, scaleFactor);
        super.onDraw(canvas);
        // paint.setStyle(Paint.Style.FILL);


        Stream.of(wires).forEach(wire -> {

            final Path path = new Path();
            path.moveTo(wire.begin.getX(), wire.begin.getY());

            float bx = wire.begin.getX();
            float by = wire.begin.getY();

            float ex = wire.end.getX();
            float ey = wire.end.getY();

            float xLength = (ex - bx);

            if (wire.begin.getConnection() == Connection.INPUT) {
                float tmp = bx;
                bx = ex;
                ex = tmp;

                tmp = by;
                by = ey;
                ey = tmp;
            }

            float xHalfLength = xLength / 2;

            float cx1 = bx + xHalfLength;
            float cx2 = ex - xHalfLength;
            path.moveTo(bx, by);
            path.cubicTo(cx1, by, cx2, ey, ex, ey);
            paint.setStrokeWidth(5);
            canvas.drawPath(path, paint);
        });
        //canvas.drawP
        canvas.drawCircle(10, 400, 50, paint);
    }

    @Override
    public boolean onDragEvent(DragEvent event) {

        BaseNode dragView = (BaseNode) event.getLocalState();

        if (event.getAction() == DragEvent.ACTION_DRAG_ENDED || event.getAction() == DragEvent.ACTION_DROP) {
            dragView.setVisibility(View.VISIBLE);
            dragView.bringToFront();
            return true;
        }
        if (event.getAction() == DragEvent.ACTION_DRAG_LOCATION) {
            //if (!event.getResult()) {

            dragView.setX(event.getX() - dragView.getPoint().x/* / dragView.getScaleX()*/);
            dragView.setY(event.getY() - dragView.getPoint().y/* / dragView.getScaleY()*/);
            //   }
        }

        return true;
    }
}

