package com.thresholdsoft.praanadhara.utils;

import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.ui.surveylistactivity.adapter.SurveyAdapter;

import java.util.List;

/**
 * Created by Jagadish on 19/05/2020.
 */

public final class BindingUtils {

    private BindingUtils() {
        // This class is not publicly instantiable
    }

    @BindingAdapter({"adapter"})
    public static void addFarmersItems(RecyclerView recyclerView, List<RowsEntity> blogs) {
        SurveyAdapter adapter = (SurveyAdapter) recyclerView.getAdapter();
        if (adapter != null && blogs != null && blogs.size() > 0) {
            adapter.clearItems();
            adapter.addItems(blogs);
        }
    }


    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Glide.with(context).load(url).into(imageView);
    }
}
