package com.thresholdsoft.wakfboard.ui.propertysurveystatus.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.wakfboard.R;
import com.thresholdsoft.wakfboard.databinding.AdapterMaptypeListBinding;
import com.thresholdsoft.wakfboard.ui.propertysurveystatus.PropertyPreview;
import com.thresholdsoft.wakfboard.ui.propertysurveystatus.PropertySurveyStatusMvpView;

import java.util.List;

public class MaptypeListAdapter extends RecyclerView.Adapter<MaptypeListAdapter.ViewHolder> {
    private Context context;
    private List<PropertyPreview.MapTypeModel> mapTypeModelList;
    private PropertySurveyStatusMvpView mListener;

    public MaptypeListAdapter(Context context, List<PropertyPreview.MapTypeModel> mapTypeModelList, PropertySurveyStatusMvpView mListener) {
        this.context = context;
        this.mapTypeModelList = mapTypeModelList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterMaptypeListBinding adapterMaptypeListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.adapter_maptype_list, parent, false);
        return new MaptypeListAdapter.ViewHolder(adapterMaptypeListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PropertyPreview.MapTypeModel mapTypeModel = mapTypeModelList.get(position);
        holder.adapterMaptypeListBinding.setModel(mapTypeModel);
        if (mapTypeModel.isSelected())
            holder.adapterMaptypeListBinding.maptypeText.setTextColor(Color.BLUE);
        else
            holder.adapterMaptypeListBinding.maptypeText.setTextColor(Color.BLACK);

        holder.adapterMaptypeListBinding.maptypeText.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mapTypeModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterMaptypeListBinding adapterMaptypeListBinding;

        public ViewHolder(@NonNull AdapterMaptypeListBinding adapterMaptypeListBinding) {
            super(adapterMaptypeListBinding.getRoot());
            this.adapterMaptypeListBinding = adapterMaptypeListBinding;
        }
    }
}
