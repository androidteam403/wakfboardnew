package com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.data.db.model.FarmerLands;
import com.thresholdsoft.praanadhara.databinding.AdapterSurveyListBinding;
import com.thresholdsoft.praanadhara.databinding.LmItemLoadingBinding;

import java.util.ArrayList;
import java.util.List;

public class SurveyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private static final DiffUtil.ItemCallback<FarmerLands> DIFF_CALLBACK = new DiffUtil.ItemCallback<FarmerLands>() {
        @Override
        public boolean areItemsTheSame(@NonNull FarmerLands oldItem, @NonNull FarmerLands newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull FarmerLands oldItem, @NonNull FarmerLands newItem) {
            return oldItem.getStatus().equals(newItem.getStatus());
        }


    };
    private List<FarmerLands> fullList;
    private AsyncListDiffer<FarmerLands> differ = new AsyncListDiffer<>(this, DIFF_CALLBACK);
    private List<FarmerLands> adapterList = new ArrayList<>();

    private final int VIEW_TYPE_ITEM = 0;
    private OnItemClickListener listener;


    public interface OnItemClickListener {
        void onItemClick(FarmerLands farmerLands);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
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
        FarmerLands farmerModel = adapterList.get(position);
        holder.adapterSurveyListBinding.setSurvey(farmerModel);

        holder.itemView.setOnClickListener(view -> {
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onItemClick(farmerModel);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        int VIEW_TYPE_LOADING = 1;
        return adapterList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    public void submitList(List<FarmerLands> stores) {
        differ.submitList(stores);
        fullList = new ArrayList<>(stores);
        adapterList.clear();
        adapterList.addAll(stores);
        notifyDataSetChanged();
    }

    public void addItemData(FarmerLands farmerLands){
        adapterList.add(null);
    }

    public void removeItemData(int position){
        adapterList.remove(position);
    }

    public int loadMorePageNumber(){
        return adapterList.get(adapterList.size()-2).getPageNo();
    }

    @Override
    public Filter getFilter() {
        return shopFilter;
    }

    private Filter shopFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<FarmerLands> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(fullList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (FarmerLands store : fullList) {
                    if (store.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(store);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //differ.getCurrentList().clear();
            //differ.getCurrentList().addAll((List) results.values);
            adapterList =(List) results.values;
            notifyDataSetChanged();
        }
    };

    public void statusFilter(String status){
        adapterList = fullList;
        if(!status.equalsIgnoreCase("")){
            if(status.equalsIgnoreCase("InProgress")){
                status = "No";
            }else if(status.equalsIgnoreCase("Completed")){
                status = "Yes";
            }
            List<FarmerLands> filteredList = new ArrayList<>();
            for (FarmerLands store : fullList) {
                if (store.getStatus().equalsIgnoreCase(status)) {
                    filteredList.add(store);
                }
            }
            adapterList = filteredList;
        }
        notifyDataSetChanged();
    }
}