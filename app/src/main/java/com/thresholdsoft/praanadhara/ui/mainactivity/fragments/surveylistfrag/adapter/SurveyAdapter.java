package com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.databinding.AdapterSurveyListBinding;
import com.thresholdsoft.praanadhara.databinding.LmItemLoadingBinding;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.SurveyListFrag;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.SurveyListMvpPresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.SurveyListMvpView;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.SurveyListModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SurveyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private ArrayList<SurveyListModel> surveyModelArrayList;
    private ArrayList<SurveyListModel> filteredSurveyModelArrayList;
    private SurveyListMvpPresenter<SurveyListMvpView> mPresenter;
    private Activity activity;
    private SurveyListFrag frag;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public SurveyAdapter(Activity activity, ArrayList<SurveyListModel> surveyModelArrayList,
                         SurveyListMvpPresenter<SurveyListMvpView> mPresenter, SurveyListFrag frag) {
        this.activity = activity;
        this.surveyModelArrayList = surveyModelArrayList;
        this.filteredSurveyModelArrayList = surveyModelArrayList;
        this.mPresenter = mPresenter;
        this.frag = frag;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            AdapterSurveyListBinding adapterSurveyListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.adapter_survey_list, parent, false);
            return new SurveyAdapter.ViewHolder(adapterSurveyListBinding);

        } else {
            LmItemLoadingBinding lmItemLoadingBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.lm_item_loading, parent, false);
            return new SurveyAdapter.LoadingViewHolder(lmItemLoadingBinding);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LmItemLoadingBinding lmItemLoadingBinding;

        public LoadingViewHolder(@NonNull LmItemLoadingBinding lmItemLoadingBinding) {
            super(lmItemLoadingBinding.getRoot());
            this.lmItemLoadingBinding = lmItemLoadingBinding;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public AdapterSurveyListBinding adapterSurveyListBinding;

        public ViewHolder(@NonNull AdapterSurveyListBinding adapterSurveyListBinding) {
            super(adapterSurveyListBinding.getRoot());
            this.adapterSurveyListBinding = adapterSurveyListBinding;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            populateItemRows((SurveyAdapter.ViewHolder) holder, position);
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((SurveyAdapter.LoadingViewHolder) holder, position);
        }
    }

    private void populateItemRows(SurveyAdapter.ViewHolder holder, int position) {
        SurveyListModel farmerModel = filteredSurveyModelArrayList.get(position);
        holder.adapterSurveyListBinding.setSurvey(farmerModel);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onItemClick(farmerModel);
            }
        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredSurveyModelArrayList = surveyModelArrayList;
                } else {
                    ArrayList<SurveyListModel> filteredList = new ArrayList<>();
                    for (SurveyListModel row : surveyModelArrayList) {
                        if (row.getName().toUpperCase().contains(charString.toUpperCase())) {
                            filteredList.add(row);
                        }
                    }
                    filteredSurveyModelArrayList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredSurveyModelArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredSurveyModelArrayList = (ArrayList<SurveyListModel>) filterResults.values;
                notifyDataSetChanged();
               // frag.updateFilteredList(filteredSurveyModelArrayList);
            }
        };
    }

    @Override
    public int getItemCount() {
        return filteredSurveyModelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return surveyModelArrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }


    public void addItems(List<SurveyListModel> blogList) {
        surveyModelArrayList.addAll(blogList);
        notifyDataSetChanged();
    }

    public void clearItems() {
        surveyModelArrayList.clear();
    }
}