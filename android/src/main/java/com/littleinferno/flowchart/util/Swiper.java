package com.littleinferno.flowchart.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Swiper extends ItemTouchHelper.SimpleCallback {

    private final Paint paint = new Paint();
    private final Map<Integer, List<Action>> actions;
    private final Context context;
    private CanSwipe canSwipe;
    private int backgroundColor;
    private int icon;
    private int iconColor;

    private Swiper(Context context) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP);
        this.context = context;
        this.actions = new HashMap<>();
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        Stream.of(actions)
                .filter(value -> value.getKey() == direction)
                .flatMap(list -> Stream.of(list.getValue()))
                .forEach(action -> action.action(position));
    }

    @Override
    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (canSwipe != null && canSwipe.swipe(viewHolder)) return 0;

        return Stream.of(ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT, ItemTouchHelper.DOWN, ItemTouchHelper.UP)
                .filter(actions::containsKey)
                .reduce((v1, v2) -> v1 | v2)
                .orElse(0);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            View itemView = viewHolder.itemView;
            float height = (float) itemView.getBottom() - (float) itemView.getTop();
            float width = height / 3;

            RectF background = null;
            RectF dest = null;

            if (dX < 0) {
                background = new RectF(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                dest = new RectF(itemView.getRight() - 2 * width, itemView.getTop() + width, itemView.getRight() - width, itemView.getBottom() - width);
            } else if (dX > 0) {
                background = new RectF(itemView.getLeft(), itemView.getTop(), dX, itemView.getBottom());
                dest = new RectF(itemView.getLeft() + width, itemView.getTop() + width, itemView.getLeft() + 2 * width, itemView.getBottom() - width);
            } else if (dY < 0) {
                background = new RectF(itemView.getLeft(), itemView.getTop() + dY, itemView.getRight(), itemView.getBottom());
                dest = new RectF(itemView.getLeft() + width, itemView.getTop() + width, itemView.getLeft() + 2 * width, itemView.getBottom() - width);// TODO
            } else if (dY > 0) {
                background = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getRight(), itemView.getBottom() + dX);
                dest = new RectF(itemView.getLeft() + width, itemView.getTop() + width, itemView.getLeft() + 2 * width, itemView.getBottom() - width);// TODO
            }

            if (background != null)
                draw(c, background, dest);
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private void draw(final Canvas canvas, final RectF background, final RectF dest) {
        paint.setColor(backgroundColor);

        canvas.drawRect(background, paint);

        Drawable drawable = ContextCompat.getDrawable(context, icon);
        drawable.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
        Bitmap bitmap = ResUtil.getBitmap((VectorDrawable) drawable);

        canvas.drawBitmap(bitmap, null, dest, paint);
    }


    public static Swiper create(Context context) {
        return new Swiper(context);
    }

    public Swiper swipeLeft(Action action) {
        registerSwipe(ItemTouchHelper.LEFT, action);
        return this;
    }

    public Swiper swipeRight(Action action) {
        registerSwipe(ItemTouchHelper.RIGHT, action);
        return this;
    }

    public Swiper swipeUp(Action action) {
        registerSwipe(ItemTouchHelper.UP, action);
        return this;
    }

    public Swiper swipeDown(Action action) {
        registerSwipe(ItemTouchHelper.DOWN, action);
        return this;
    }

    private void registerSwipe(int direction, Action action) {
        List<Action> list = this.actions.get(direction);
        if (list == null)
            list = new ArrayList<>();

        list.add(action);
        actions.put(direction, list);
    }

    public Swiper swipeRule(CanSwipe canSwipe) {
        this.canSwipe = canSwipe;
        return this;
    }

    public Swiper icon(final int icon) {
        this.icon = icon;
        return this;
    }

    public Swiper backgroundColor(final int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public Swiper iconColor(final int iconColor) {
        this.iconColor = iconColor;
        return this;
    }

    public void attachTo(RecyclerView recyclerView) {
        new ItemTouchHelper(this).attachToRecyclerView(recyclerView);
    }

    public interface Action {
        void action(final int position);
    }

    public interface CanSwipe {
        boolean swipe(RecyclerView.ViewHolder viewHolder);
    }

}
