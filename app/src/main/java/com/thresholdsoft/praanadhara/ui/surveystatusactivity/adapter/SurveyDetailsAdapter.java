package com.thresholdsoft.praanadhara.ui.surveystatusactivity.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyDetailsEntity;
import com.thresholdsoft.praanadhara.databinding.ViewSurveyDetailsBinding;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusMvpPresenter;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusMvpView;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.dialog.CustomEditDialog;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.dialog.deletedialog.DeleteDialog;

import java.util.ArrayList;
import java.util.List;

public class SurveyDetailsAdapter extends RecyclerView.Adapter<SurveyDetailsAdapter.ViewHolder> {

    private ArrayList<SurveyDetailsEntity> surveyModelArrayList;
    private SurveyStatusMvpPresenter<SurveyStatusMvpView> mPresenter;
    private Activity activity;
    private SurveyStatusMvpView statusMvpView;


    public SurveyDetailsAdapter(Activity activity, ArrayList<SurveyDetailsEntity> surveyModelArrayList,
                                SurveyStatusMvpPresenter<SurveyStatusMvpView> mPresenter, SurveyStatusMvpView statusMvpView) {
        this.activity = activity;
        this.surveyModelArrayList = surveyModelArrayList;
        this.mPresenter = mPresenter;
        this.statusMvpView = statusMvpView;
    }

    @NonNull
    @Override
    public SurveyDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewSurveyDetailsBinding adapterSurveyListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.view_survey_details, parent, false);
        return new SurveyDetailsAdapter.ViewHolder(adapterSurveyListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final SurveyDetailsAdapter.ViewHolder holder, int position) {
        SurveyDetailsEntity farmerModel = surveyModelArrayList.get(position);
        holder.adapterSurveyListBinding.setData(farmerModel);

        if (!farmerModel.isUnChecked()) {
            holder.adapterSurveyListBinding.checkBox.setChecked(true);
        } else {
            holder.adapterSurveyListBinding.checkBox.setChecked(false);
        }

        holder.adapterSurveyListBinding.checkBox.setOnClickListener(view -> {
            statusMvpView.onListItemClicked(position);
        });
        holder.adapterSurveyListBinding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentManager manager = ((AppCompatActivity) activity).getSupportFragmentManager();
//                EditDialog dialog = new EditDialog();
//                dialog.show(manager, "editdialog");
                CustomEditDialog customEditDialog = new CustomEditDialog(activity);
                customEditDialog.setEditTextData(farmerModel.getName());
                customEditDialog.setTitle("Edit ");
                customEditDialog.setPositiveUpdateLabel("Update");
                customEditDialog.setPositiveUpdateListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        holder.adapterSurveyListBinding.checkBox.setText(customEditDialog.getPointName());
                        farmerModel.setName(customEditDialog.getPointName());
                        customEditDialog.dismiss();
                        mPresenter.editApiCal(farmerModel,position);
                    }
                });
                customEditDialog.setNegativeUpdateLabel("Back");
                customEditDialog.setNegativeUpdateListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customEditDialog.dismiss();
                    }
                });
                customEditDialog.show();
            }
        });
        holder.adapterSurveyListBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DeleteDialog deleteDialog = new DeleteDialog(activity);
                deleteDialog.setTitle("Are You Sure!");
                deleteDialog.setPositiveLabel("Ok");
                deleteDialog.setPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.deleteApiCall(farmerModel, position);
//                        statusMvpView.deleteAnItem(position);
//                        surveyModelArrayList.remove(farmerModel);
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
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewSurveyDetailsBinding adapterSurveyListBinding;

        public ViewHolder(@NonNull ViewSurveyDetailsBinding adapterSurveyListBinding) {
            super(adapterSurveyListBinding.getRoot());
            this.adapterSurveyListBinding = adapterSurveyListBinding;
        }
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
}
