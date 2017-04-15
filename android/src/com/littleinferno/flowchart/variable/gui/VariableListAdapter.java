package com.littleinferno.flowchart.variable.gui;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionRemoveItem;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;
import com.littleinferno.flowchart.R;
import com.littleinferno.flowchart.variable.AndroidVariable;

import java.util.List;

public class VariableListAdapter
        extends RecyclerView.Adapter<VariableListAdapter.ViewHolder>
        implements SwipeableItemAdapter<VariableListAdapter.ViewHolder> {

    private final List<AndroidVariable> variables;
    private EventListener eventListener;


    public VariableListAdapter(List<AndroidVariable> variables) {
        this.variables = variables;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_variable, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AndroidVariable variable = variables.get(position);

        //  holder.itemView.setOnClickListener(mItemViewOnClickListener);
        // holder.container.setOnClickListener(mSwipeableViewContainerOnClickListener);

        holder.varName.setText(variable.getName());
        holder.varType.setText(variable.getDataType().toString());
        holder.varArray.setText(String.valueOf(variable.isArray()));

        // set background resource (target view ID: container)
        final int swipeState = holder.getSwipeStateFlags();

//        if ((swipeState & Swipeable.STATE_FLAG_IS_UPDATED) != 0) {
//            int bgResId;
//
//            if ((swipeState & Swipeable.STATE_FLAG_IS_ACTIVE) != 0) {
//                bgResId = R.drawable.bg_item_swiping_active_state;
//            } else if ((swipeState & Swipeable.STATE_FLAG_SWIPING) != 0) {
//                bgResId = R.drawable.bg_item_swiping_state;
//            } else {
//                bgResId = R.drawable.bg_item_normal_state;
//            }
//
//            holder.container.setBackgroundResource(bgResId);
//        }

        //    holder.setSwipeItemHorizontalSlideAmount(
        //            variable.isPinned() ? Swipeable.OUTSIDE_OF_THE_WINDOW_LEFT : 0);
    }

    @Override
    public int getItemCount() {
        return variables.size();
    }

    @Override
    public int onGetSwipeReactionType(ViewHolder holder, int position, int x, int y) {
        return SwipeableItemConstants.REACTION_CAN_SWIPE_RIGHT |
                SwipeableItemConstants.REACTION_MASK_START_SWIPE_RIGHT;
//                SwipeableItemConstants.REACTION_START_SWIPE_ON_LONG_PRESS;
    }

    @Override
    public void onSetSwipeBackground(ViewHolder holder, int position, int type) {

    }

    @Override
    public SwipeResultAction onSwipeItem(ViewHolder holder, int position, int result) {
        if (result == SwipeableItemConstants.RESULT_SWIPED_RIGHT)
            return new SwipeRightResultAction(this, position);
        return null;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public interface EventListener {
        void onItemRemoved(int position);

        void onItemPinned(int position);

        void onItemViewClicked(View v, boolean pinned);
    }

    public static class ViewHolder extends AbstractSwipeableItemViewHolder {

        private final CardView container;
        private final TextView varName;
        private final TextView varType;
        private final TextView varArray;

        public ViewHolder(View v) {
            super(v);
            container = (CardView) v.findViewById(R.id.container);
            varName = (TextView) container.findViewById(R.id.variable_name);
            varType = (TextView) container.findViewById(R.id.variable_type);
            varArray = (TextView) container.findViewById(R.id.variable_array);
        }

        @Override
        public View getSwipeableContainerView() {
            return container;
        }
    }

    private static class SwipeRightResultAction extends SwipeResultActionRemoveItem {
        private VariableListAdapter adapter;
        private final int position;

        SwipeRightResultAction(VariableListAdapter adapter, int position) {
            this.adapter = adapter;
            this.position = position;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            Log.wtf("QWE", "REMOVED");
            adapter.variables.remove(position);
            adapter.notifyItemRemoved(position);
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();

            if (adapter.eventListener != null) {
                adapter.eventListener.onItemRemoved(position);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            adapter = null;
        }
    }
}
