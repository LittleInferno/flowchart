package com.littleinferno.flowchart.scene;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;


public class SceneDrag extends CardView {

    public SceneDrag(Context context, AttributeSet attrs) {
        super(context, attrs);
        delta = new PointF();

//        post(() -> {
//            setX(getWidth() / 2);
//            setY(getHeight() / 2);
//        });
        scaleDetector = new ScaleGestureDetector(context, new  ScaleListener());

    }

    private PointF delta;
    private final ScaleGestureDetector scaleDetector;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                delta.set(getX() - event.getRawX(), getY() - event.getRawY());

                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float x = event.getRawX() + delta.x;
                if (x > -850 && x < 200)
                    setX(x);

                float y = event.getRawY() + delta.y;
                if (y > -850 && y < 850)
                    setY(y);

                break;
            }
        }

        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        private float scaleFactor = 1;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();

            scaleFactor = Math.max(0.5f, Math.min(scaleFactor, 1.5f));

            setScaleX(scaleFactor);
            setScaleY(scaleFactor);

            invalidate();
            return true;
        }
    }

}

