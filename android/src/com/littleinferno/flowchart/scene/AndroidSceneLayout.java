package com.littleinferno.flowchart.scene;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.node.BaseNode;
import com.littleinferno.flowchart.pin.Connector;

import java.util.ArrayList;

public class AndroidSceneLayout extends RelativeLayout {
    private final Paint paint;

    private int x = 0;
    private int y = 0;
    boolean touch = false;

    private static class Wire {
        final Connector begin, end;

        private Wire(Connector begin, Connector end) {
            this.begin = begin;
            this.end = end;
        }
    }

    ArrayList<Wire> wires = new ArrayList<>();

    public AndroidSceneLayout(Context context, String sceneType) {
        super(context);
        paint = new Paint();
        setWillNotDraw(false);
    }

    public AndroidSceneLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        setWillNotDraw(false);
    }

    @Override
    public boolean dispatchDragEvent(DragEvent event) {
        return super.dispatchDragEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touch = true;
            x = (int) event.getX();
            y = (int) event.getY();

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            touch = false;

        }

//        super.onTouchEvent(event);

//        gestureDetector.onTouchEvent(event);
//        scaleDetector.onTouchEvent(event);

        return ((CardView) getParent()).onTouchEvent(event);
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

    @Override
    protected void onDraw(Canvas canvas) {

        //  if (update)
        {
            // canvas.translate(delta.x, delta.y);
        }
//        canvas.scale(scaleFactor, scaleFactor);
        super.onDraw(canvas);
        // paint.setStyle(Paint.Style.FILL);


//        Stream.of(wires).forEach(wire -> {
//
//            final Path path = new Path();
//            path.moveTo(wire.begin.getX(), wire.begin.getY());
//
//            float bx = wire.begin.getX();
//            float by = wire.begin.getY();
//
//            float ex = wire.end.getX();
//            float ey = wire.end.getY();
//
//            float xLength = (ex - bx);
//
//            if (wire.begin.getConnection() == Connection.INPUT) {
//                float tmp = bx;
//                bx = ex;
//                ex = tmp;
//
//                tmp = by;
//                by = ey;
//                ey = tmp;
//            }
//
//            float xHalfLength = xLength / 2;
//
//            float cx1 = bx + xHalfLength;
//            float cx2 = ex - xHalfLength;
//            path.moveTo(bx, by);
//            path.cubicTo(cx1, by, cx2, ey, ex, ey);
//            paint.setStrokeWidth(5);
//            canvas.drawPath(path, paint);
//        });
        //canvas.drawP
        canvas.drawCircle(50, x, y, paint);
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
            invalidate();
            //   }
        }

        return true;
    }
}

