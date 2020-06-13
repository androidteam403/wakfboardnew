package com.thresholdsoft.praanadhara.ui.surveystatusactivity.adapter;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.loopeer.itemtouchhelperextension.Extension;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;
import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.data.db.model.SurveyEntity;
import com.thresholdsoft.praanadhara.databinding.ListItemMainBinding;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusMvpView;

import java.util.List;

public class SurveyDetailsAdapter extends RecyclerView.Adapter<SurveyDetailsAdapter.ItemBaseViewHolder> {
    private SurveyStatusMvpView statusMvpView;
    private ItemTouchHelperExtension mItemTouchHelperExtension;

    public SurveyDetailsAdapter(SurveyStatusMvpView statusMvpView) {
        this.statusMvpView = statusMvpView;
    }

    public void setItemTouchHelperExtension(ItemTouchHelperExtension itemTouchHelperExtension) {
        mItemTouchHelperExtension = itemTouchHelperExtension;
    }


    @NonNull
    @Override
    public SurveyDetailsAdapter.ItemBaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemMainBinding listItemMainBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.list_item_main, parent, false);
        return new ItemSwipeWithActionWidthViewHolder(listItemMainBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final SurveyDetailsAdapter.ItemBaseViewHolder holder, int position) {
        SurveyEntity farmerModel = differ.getCurrentList().get(position);
        holder.listItemMainBinding.setData(farmerModel);
        holder.listItemMainBinding.cartlayout.setData(farmerModel);

        if(farmerModel.getMapType()!=null) {
            if (farmerModel.getMapType().equalsIgnoreCase("point")) {
                holder.listItemMainBinding.cartlayout.image.setBackgroundResource(R.drawable.new_point);
            } else if (farmerModel.getMapType().equalsIgnoreCase("line")) {
                holder.listItemMainBinding.cartlayout.image.setBackgroundResource(R.drawable.new_line);
            } else if (farmerModel.getMapType().equalsIgnoreCase("polygon")) {
                holder.listItemMainBinding.cartlayout.image.setBackgroundResource(R.drawable.new_polygon);
            }
        }
        if (!farmerModel.isUnchecked()) {
            holder.listItemMainBinding.cartlayout.checkBox.setChecked(true);
        } else {
            holder.listItemMainBinding.cartlayout.checkBox.setChecked(false);
        }

        holder.listItemMainBinding.cartlayout.checkBox.setOnClickListener(view -> statusMvpView.onListItemClicked(farmerModel));

        if (holder instanceof ItemSwipeWithActionWidthViewHolder) {
            ItemSwipeWithActionWidthViewHolder viewHolder = (ItemSwipeWithActionWidthViewHolder) holder;
            viewHolder.mActionViewDelete.setOnClickListener(
                    view -> {
                        viewHolder.listItemMainBinding.viewListRepoActionContainer.setVisibility(View.GONE);
                        statusMvpView.onClickDeleteSurvey(farmerModel);
                        mItemTouchHelperExtension.closeOpened();
                    }
            );
            viewHolder.mActionViewEdit.setOnClickListener(
                    view -> {
                        viewHolder.listItemMainBinding.viewListRepoActionContainer.setVisibility(View.GONE);
                        statusMvpView.onClickEditSurvey(farmerModel);
                        mItemTouchHelperExtension.closeOpened();
                    }
            );
        }
    }

    public void move(int from, int to) {
        SurveyEntity prev = differ.getCurrentList().remove(from);
        differ.getCurrentList().add(to > from ? to - 1 : to, prev);
        notifyItemMoved(from, to);
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void addItems(List<SurveyEntity> blogList) {
        differ.submitList(blogList);
    }

    public List<SurveyEntity> getListData(){
       return differ.getCurrentList();
    }

    public static class ItemBaseViewHolder extends RecyclerView.ViewHolder {
        public ListItemMainBinding listItemMainBinding;

        public ItemBaseViewHolder(ListItemMainBinding itemView) {
            super(itemView.getRoot());
            listItemMainBinding = itemView;
        }
    }

    class ItemSwipeWithActionWidthViewHolder extends ItemBaseViewHolder implements Extension {

        View mActionViewDelete;
        View mActionViewEdit;


        public ItemSwipeWithActionWidthViewHolder(ListItemMainBinding itemView) {
            super(itemView);
            mActionViewDelete = itemView.getRoot().findViewById(R.id.view_list_repo_action_delete);
            mActionViewEdit = itemView.getRoot().findViewById(R.id.view_list_repo_action_update);
        }

        @Override
        public float getActionWidth() {
            listItemMainBinding.viewListRepoActionContainer.setVisibility(View.VISIBLE);
            postDelay();
            return listItemMainBinding.viewListRepoActionContainer.getWidth();
        }

    }

    private void postDelay() {
        Handler handler = new Handler();
        handler.postDelayed(this::notifyDataSetChanged, 100);
    }


    private static final DiffUtil.ItemCallback<SurveyEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<SurveyEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull SurveyEntity oldItem, @NonNull SurveyEntity newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull SurveyEntity oldItem, @NonNull SurveyEntity newItem) {
            return oldItem.getName().equals(newItem.getName());
        }


    };

    private AsyncListDiffer<SurveyEntity> differ = new AsyncListDiffer<>(this, DIFF_CALLBACK);
}
