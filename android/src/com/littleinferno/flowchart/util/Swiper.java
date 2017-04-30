package com.littleinferno.flowchart.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.littleinferno.flowchart.R;

public class Swiper extends ItemTouchHelper.SimpleCallback {
    private final Paint paint = new Paint();
    private Action action;
    private Context context;

    public Swiper(Context context, int direction) {
        super(0, direction);
        this.context = context;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        action.action(direction, position);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            View itemView = viewHolder.itemView;
            float height = (float) itemView.getBottom() - (float) itemView.getTop();
            float width = height / 3;

            if (dX < 0) {
                paint.setColor(Color.parseColor("#D50000"));
                RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                c.drawRect(background, paint);

                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_delete);
                Bitmap bitmap = ResUtil.getBitmap((VectorDrawable) drawable);
                RectF icon_dest = new RectF(itemView.getRight() - 2 * width, itemView.getTop() + width, itemView.getRight() - width, itemView.getBottom() - width);

                c.drawBitmap(bitmap, null, icon_dest, paint);
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    public static Swiper create(Context context, int direction) {
        return new Swiper(context, direction);
    }

    public Swiper register(Action action) {
        this.action = action;
        return this;
    }

    public void attachTo(RecyclerView recyclerView) {
        new ItemTouchHelper(this).attachToRecyclerView(recyclerView);
    }

    public interface Action {
        void action(int direction, int position);
    }

}
