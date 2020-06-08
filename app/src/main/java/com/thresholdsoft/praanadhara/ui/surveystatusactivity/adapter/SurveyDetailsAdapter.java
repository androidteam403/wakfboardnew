package com.thresholdsoft.praanadhara.ui.surveystatusactivity.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.databinding.ViewSurveyDetailsBinding;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusMvpPresenter;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.SurveyStatusMvpView;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.dialog.deletedialog.DeleteDialog;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.dialog.editdialog.EditDialog;
import com.thresholdsoft.praanadhara.ui.surveytrack.model.SurveyModel;

import java.util.ArrayList;
import java.util.List;

public class SurveyDetailsAdapter extends RecyclerView.Adapter<SurveyDetailsAdapter.ViewHolder> {

    private ArrayList<SurveyModel> surveyModelArrayList;
    private SurveyStatusMvpPresenter<SurveyStatusMvpView> mPresenter;
    private Activity activity;
    private SurveyStatusMvpView statusMvpView;


    public SurveyDetailsAdapter(Activity activity, ArrayList<SurveyModel> surveyModelArrayList,
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
        SurveyModel farmerModel = surveyModelArrayList.get(position);
        holder.adapterSurveyListBinding.setData(farmerModel);

        if (farmerModel.isChecked()) {
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
                FragmentManager manager = ((AppCompatActivity) activity).getSupportFragmentManager();
                EditDialog dialog = new EditDialog();
                dialog.show(manager, "editdialog");
            }
        });
        holder.adapterSurveyListBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = ((AppCompatActivity) activity).getSupportFragmentManager();
                DeleteDialog dialog = new DeleteDialog();
                dialog.show(manager, "deletedialog");            }
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

    public void addItems(List<SurveyModel> blogList) {
        surveyModelArrayList.addAll(blogList);
        notifyDataSetChanged();
    }

    public void clearItems() {
        surveyModelArrayList.clear();
    }
}
