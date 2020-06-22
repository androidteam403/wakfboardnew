package com.thresholdsoft.praanadhara.ui.surveystatusactivity.swipe;

import android.graphics.Canvas;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.adapter.SurveyDetailsAdapter;

public class ItemTouchHelperCallback extends ItemTouchHelperExtension.Callback {


    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.END);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        SurveyDetailsAdapter adapter = (SurveyDetailsAdapter) recyclerView.getAdapter();
        adapter.move(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (dY != 0 && dX == 0)
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        SurveyDetailsAdapter.ItemBaseViewHolder holder = (SurveyDetailsAdapter.ItemBaseViewHolder) viewHolder;
        holder.listItemMainBinding.cartlayout.viewListMainContent.setTranslationX(dX);

    }


//    @Override
//    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//        return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.START);
//    }
//
//    @Override
//    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//        ItemListAdapter adapter = (ItemListAdapter) recyclerView.getAdapter();
//        adapter.move(viewHolder.getAdapterPosition(), target.getAdapterPosition());
//        return true;
//    }
//
//    @Override
//    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//
//    }
//
//    @Override
//    public boolean isLongPressDragEnabled() {
//        return true;
//    }
//    @Override
//    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        if (dY != 0 && dX == 0) super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//        ItemListAdapter.ItemBaseViewHolder holder = (ItemListAdapter.ItemBaseViewHolder) viewHolder;
//        holder.listItemMainBinding.cartlayout.viewListMainContent.setTranslationX(dX);
//    }

}
