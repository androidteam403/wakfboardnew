package com.thresholdsoft.praanadhara.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    public static void setImageUrl(ImageView imageView, String picEntity) {
        if(!TextUtils.isEmpty(picEntity)) {
            Context context = imageView.getContext();
            Glide.with(context).load(BuildConfig.IMAGE_URL + picEntity).placeholder(R.drawable.placeholder).into(imageView);
        }
    }

    @BindingAdapter("statustext")
    public static void setStatusText(TextView statusText, String status){
        if(status.equalsIgnoreCase("New")){
            statusText.setText("New");
            statusText.setTextColor(statusText.getResources().getColor(R.color.survey_new_status_color));
        }else if(status.equalsIgnoreCase("No")){
            statusText.setText("In Progress");
            statusText.setTextColor(statusText.getResources().getColor(R.color.survey_inprogress_status_color));
        }else if(status.equalsIgnoreCase("Yes")){
            statusText.setText("Completed");
            statusText.setTextColor(statusText.getResources().getColor(R.color.survey_complete_status_color));
        }
    }

    @BindingAdapter("surveyMode")
    public static void setSurveyMode(TextView surveyText, String surveyMode){
        if(surveyMode.equalsIgnoreCase("New")){
            surveyText.setText("TAKE SURVEY");
            surveyText.setBackgroundResource(R.drawable.button_light_blue_rounde);
            surveyText.setCompoundDrawables(null,null,null,null);
        }else if(surveyMode.equalsIgnoreCase("No")) {
            surveyText.setText("CONTINUE");
            surveyText.setBackgroundResource(R.drawable.button_back_orange);
            surveyText.setCompoundDrawables(null,null,null,null);
        }else if(surveyMode.equalsIgnoreCase("Yes")) {
            surveyText.setText("DONE");
            surveyText.setBackgroundResource(R.drawable.button_back_green);
            surveyText.setCompoundDrawables(surveyText.getResources().getDrawable(R.drawable.tick_mark),null,null,null);
        }
    }

    @BindingAdapter("backgroundbox")
    public static void setBackgroundBox(LinearLayout linearLayout, String status){
        if(status.equalsIgnoreCase("New")){
            linearLayout.setBackgroundResource(R.drawable.adapter_survey_background);
        }else if(status.equalsIgnoreCase("No")){
            linearLayout.setBackgroundResource(R.drawable.adapter_survey_back_orange);
        }else if(status.equalsIgnoreCase("Yes")){
            linearLayout.setBackgroundResource(R.drawable.adapter_survey_back_green);
        }
    }

    @BindingAdapter({"surveyStatus","surveyStartDate","surveySubmitDate"})
    public static void setSurveyDate(TextView textView, String status, String startDate, String submitDate){
        if(status.equalsIgnoreCase("New")){
            textView.setText("");
        }else if(status.equalsIgnoreCase("No")){
            textView.setText(CommonUtils.dateConversion(startDate));
        }else if(status.equalsIgnoreCase("Yes")){
            textView.setText(CommonUtils.dateConversion(submitDate));
        }
    }
}