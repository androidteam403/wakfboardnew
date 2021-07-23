package com.thresholdsoft.wakfboard.ui.mapdataliastactivity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.wakfboard.R;
import com.thresholdsoft.wakfboard.databinding.AdapterMapDataListBinding;
import com.thresholdsoft.wakfboard.ui.mapdataliastactivity.MapDataListActivityMvpView;
import com.thresholdsoft.wakfboard.ui.propertysurvey.model.MapDataTable;

import java.util.List;

public class MapDataAdapter extends RecyclerView.Adapter<MapDataAdapter.ViewHolder> {
    private Context context;
    private List<MapDataTable> mapDataTableList;
    private MapDataListActivityMvpView mapDataListActivityMvpView;

    public MapDataAdapter(Context context, List<MapDataTable> mapDataTableList, MapDataListActivityMvpView mapDataListActivityMvpView) {
        this.context = context;
        this.mapDataTableList = mapDataTableList;
        this.mapDataListActivityMvpView = mapDataListActivityMvpView;
    }

    @NonNull
    @Override
    public MapDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterMapDataListBinding adapterMapDataListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.adapter_map_data_list, parent, false);
        return new MapDataAdapter.ViewHolder(adapterMapDataListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MapDataAdapter.ViewHolder holder, int position) {
        MapDataTable mapDataTable = mapDataTableList.get(position);
        holder.adapterMapDataListBinding.setMapData(mapDataTable);
        if (mapDataTable.getMapType() == 1) {
            holder.adapterMapDataListBinding.image.setImageResource(R.drawable.new_point);
        } else if (mapDataTable.getMapType() == 2) {
            holder.adapterMapDataListBinding.image.setImageResource(R.drawable.new_line);
        } else if (mapDataTable.getMapType() == 3) {
            holder.adapterMapDataListBinding.image.setImageResource(R.drawable.new_polygon);
        }

        holder.adapterMapDataListBinding.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapDataListActivityMvpView != null) {
                    mapDataListActivityMvpView.uncheckableData(position, mapDataTableList);
                }
            }
        });

        holder.adapterMapDataListBinding.itemViewClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapDataListActivityMvpView != null) {
                    mapDataListActivityMvpView.onClickEditMapView(position, mapDataTableList);
                }
            }
        });

        holder.adapterMapDataListBinding.imagesIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapDataListActivityMvpView != null) {
                    mapDataListActivityMvpView.onClickImageShow(position, mapDataTableList);
                }
            }
        });


        if (mapDataTable.getPointPhotoData() != null && mapDataTable.getPointPhotoData().size() > 0) {
            AdapterImagesList adapterImagesList = new AdapterImagesList(context, mapDataTable.getPointPhotoData());
            RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            holder.adapterMapDataListBinding.imagesRecycleList.setLayoutManager(mLayoutManager2);
            holder.adapterMapDataListBinding.imagesRecycleList.setItemAnimator(new DefaultItemAnimator());
            holder.adapterMapDataListBinding.imagesRecycleList.setAdapter(adapterImagesList);
            holder.adapterMapDataListBinding.imagesRecycleList.setNestedScrollingEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return mapDataTableList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterMapDataListBinding adapterMapDataListBinding;

        public ViewHolder(@NonNull AdapterMapDataListBinding adapterMapDataListBinding) {
            super(adapterMapDataListBinding.getRoot());
            this.adapterMapDataListBinding = adapterMapDataListBinding;
        }
    }

}