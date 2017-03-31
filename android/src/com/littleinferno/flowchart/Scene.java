package com.littleinferno.flowchart;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.RelativeLayout;

public class Scene extends RelativeLayout {
    private final Paint paint;
    private final GestureDetector gestureDetector;
    private final ScaleGestureDetector scaleDetector;

    public Scene(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        gestureDetector = new GestureDetector(context, new GestureListener());
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        scaleDetector.onTouchEvent(event);
        return true;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            scrollBy((int) distanceX, (int) distanceY);
            return true;
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private float scaleFactor = 1;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();

            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));


            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);

                child.setScaleX(scaleFactor);
                child.setScaleY(scaleFactor);
            }

            invalidate();
            return true;
        }
    }


}
