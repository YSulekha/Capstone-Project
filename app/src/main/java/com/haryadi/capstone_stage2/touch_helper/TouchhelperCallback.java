package com.haryadi.capstone_stage2.touch_helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class TouchhelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;
    public static final float ALPHA_FULL = 1.0f;
    Context context;
    int mDirection;

    public TouchhelperCallback(ItemTouchHelperAdapter adapter,Context c){
        mAdapter = adapter;
        context = c;
    }
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START;
        return makeMovementFlags(dragFlags,swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }



    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if(direction == ItemTouchHelper.START){
            mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        }
    }
}
