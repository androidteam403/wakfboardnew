package com.thresholdsoft.praanadhara.utils;

import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thresholdsoft.praanadhara.BuildConfig;
import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.data.network.pojo.PicEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.adapter.SurveyAdapter;

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
//            adapter.clearItems();
//            adapter.addItems(blogs);
        }
    }

    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, List<PicEntity> picEntity) {
        if(picEntity.size() > 0) {
            Context context = imageView.getContext();
            Glide.with(context).load(BuildConfig.IMAGE_URL + picEntity.get(0).getPath()).placeholder(R.drawable.placeholder).into(imageView);
        }
    }
}