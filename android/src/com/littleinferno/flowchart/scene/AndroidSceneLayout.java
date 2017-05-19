package com.littleinferno.flowchart.scene;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.node.AndroidNode;
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

    public AndroidSceneLayout(Context context) {
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
            invalidate();

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            touch = false;

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            x = (int) event.getX();
            y = (int) event.getY();
            invalidate();
        }

//        super.onTouchEvent(event);

//        gestureDetector.onTouchEvent(event);
//        scaleDetector.onTouchEvent(event);

        return ((CardView) getParent()).onTouchEvent(event);
    }

    public void addWire(Connector connector, Connector pin) {
        wires.add(new Wire(connector, pin));
        invalidate();
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
        super.onDraw(canvas);

        Stream.of(wires).forEach(wire -> {

            float bx = wire.begin.getSceneX();
            float by = wire.begin.getSceneY();

            float ex = wire.end.getSceneX();
            float ey = wire.end.getSceneY();

            paint.setStrokeWidth(10);

//            paint.setColor(ResUtil.getDataTypeColor(getContext(), wire.begin.getType()));

            canvas.drawLine(bx, by, ex, ey, paint);
        });
    }

    @Override
    public boolean onDragEvent(DragEvent event) {

        AndroidNode dragView = (AndroidNode) event.getLocalState();

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

