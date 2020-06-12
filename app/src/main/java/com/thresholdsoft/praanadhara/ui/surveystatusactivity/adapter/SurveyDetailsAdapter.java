package com.thresholdsoft.praanadhara.ui.surveystatusactivity.adapter;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.loopeer.itemtouchhelperextension.Extension;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;
import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyDetailsEntity;
import com.thresholdsoft.praanadhara.databinding.ListItemMainBinding;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusMvpPresenter;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusMvpView;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.dialog.CustomEditDialog;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.dialog.deletedialog.DeleteDialog;

import java.util.ArrayList;
import java.util.List;

public class SurveyDetailsAdapter extends RecyclerView.Adapter<SurveyDetailsAdapter.ItemBaseViewHolder> {
    private ArrayList<SurveyDetailsEntity> surveyModelArrayList;
    private SurveyStatusMvpPresenter<SurveyStatusMvpView> mPresenter;
    private Activity activity;
    private SurveyStatusMvpView statusMvpView;
    private ItemTouchHelperExtension mItemTouchHelperExtension;
    private CustomEditDialog customEditDialog;
    private DeleteDialog deleteDialog;

    public SurveyDetailsAdapter(Activity activity, ArrayList<SurveyDetailsEntity> surveyModelArrayList,
                                SurveyStatusMvpPresenter<SurveyStatusMvpView> mPresenter, SurveyStatusMvpView statusMvpView) {
        this.activity = activity;
        this.surveyModelArrayList = surveyModelArrayList;
        this.mPresenter = mPresenter;
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
        SurveyDetailsEntity farmerModel = surveyModelArrayList.get(position);
        holder.listItemMainBinding.setData(farmerModel);
        holder.listItemMainBinding.cartlayout.setData(farmerModel);

        if (farmerModel.getMapType().getName().equalsIgnoreCase("point")) {
            holder.listItemMainBinding.cartlayout.image.setBackgroundResource(R.drawable.new_point);
        } else if (farmerModel.getMapType().getName().equalsIgnoreCase("line")) {
            holder.listItemMainBinding.cartlayout.image.setBackgroundResource(R.drawable.new_line);
        } else if (farmerModel.getMapType().getName().equalsIgnoreCase("polygon")) {
            holder.listItemMainBinding.cartlayout.image.setBackgroundResource(R.drawable.new_polygon);
        }

        if (!farmerModel.isUnChecked()) {
            holder.listItemMainBinding.cartlayout.checkBox.setChecked(true);
        } else {
            holder.listItemMainBinding.cartlayout.checkBox.setChecked(false);
        }

        holder.listItemMainBinding.cartlayout.checkBox.setOnClickListener(view -> {
            statusMvpView.onListItemClicked(position);
        });

        if (holder instanceof ItemSwipeWithActionWidthViewHolder) {
            ItemSwipeWithActionWidthViewHolder viewHolder = (ItemSwipeWithActionWidthViewHolder) holder;
            viewHolder.mActionViewDelete.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewHolder.listItemMainBinding.viewListRepoActionContainer.setVisibility(View.GONE);
                            deleteDialog = new DeleteDialog(activity);
                            deleteDialog.setTitle("Are You Sure!");
                            deleteDialog.setPositiveLabel("Ok");
                            deleteDialog.setPositiveListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mPresenter.deleteApiCall(farmerModel, position);
                                    deleteDialog.dismiss();
                                }
                            });
                            deleteDialog.setNegativeLabel("Cancel");
                            deleteDialog.setNegativeListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteDialog.dismiss();
                                }
                            });
                            deleteDialog.show();
                            mItemTouchHelperExtension.closeOpened();
                        }
                    }
            );
            viewHolder.mActionViewEdit.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewHolder.listItemMainBinding.viewListRepoActionContainer.setVisibility(View.GONE);
                            customEditDialog = new CustomEditDialog(activity);
                            customEditDialog.setEditTextData(farmerModel.getName());
                            customEditDialog.setEditTextDescriptionData(farmerModel.getDescription());
                            customEditDialog.setTitle("Edit ");
                            customEditDialog.setPositiveUpdateLabel("Update");
                            customEditDialog.setPositiveUpdateListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                        holder.adapterSurveyListBinding.checkBox.setText(customEditDialog.getPointName());
                                    if (customEditDialog.validations()) {
                                        farmerModel.setName(customEditDialog.getPointName());
                                        farmerModel.setDescription(customEditDialog.getPointDescription());
                                        customEditDialog.dismiss();
                                        mPresenter.editApiCal(farmerModel, position);
                                    }
                                }
                            });
                            customEditDialog.setNegativeUpdateLabel("Cancel");
                            customEditDialog.setNegativeUpdateListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    customEditDialog.dismiss();
                                }
                            });
                            customEditDialog.show();
                            mItemTouchHelperExtension.closeOpened();
                        }
                    }
            );
        }
    }

    public void move(int from, int to) {
        SurveyDetailsEntity prev = surveyModelArrayList.remove(from);
        surveyModelArrayList.add(to > from ? to - 1 : to, prev);
        notifyItemMoved(from, to);
    }

    @Override
    public int getItemCount() {
        return surveyModelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void addItems(List<SurveyDetailsEntity> blogList) {
        surveyModelArrayList.addAll(blogList);
        notifyDataSetChanged();
    }

    public void clearItems() {
        surveyModelArrayList.clear();
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
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        }, 100);
    }

    private boolean validations() {
        String name = customEditDialog.getPointName();
        String description = customEditDialog.getPointDescription();
        if (name.isEmpty()) {
            return false;
        } else if (description.isEmpty()) {
            return false;
        }
        return true;
    }

}
