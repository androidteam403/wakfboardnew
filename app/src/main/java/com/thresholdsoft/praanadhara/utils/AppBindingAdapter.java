package com.thresholdsoft.praanadhara.utils;

import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.databinding.BindingAdapter;

import com.thresholdsoft.praanadhara.R;


public class AppBindingAdapter {

    @BindingAdapter({"app:full_text", "app:span_text", "app:span_color"})
    public static void formatText(TextView textView, String full_text, String span_text, int span_color) {
        int firstMatchingIndex = full_text.indexOf(span_text);
        int lastMatchingIndex = firstMatchingIndex + span_text.length();
        SpannableString spannable = new SpannableString(full_text);
        spannable.setSpan(new ForegroundColorSpan(span_color), firstMatchingIndex, lastMatchingIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannable);
    }

    @BindingAdapter({"app:full_text", "app:span_text", "app:span_color", "app:span_size"})
    public static void formatTextWithSize(TextView textView, String full_text, String span_text, int span_color, float spanSize) {
        int firstMatchingIndex = full_text.indexOf(span_text);
        int lastMatchingIndex = firstMatchingIndex + span_text.length();
        SpannableString spannable = new SpannableString(full_text);
        spannable.setSpan(new RelativeSizeSpan(spanSize), firstMatchingIndex, lastMatchingIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(span_color), firstMatchingIndex, lastMatchingIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannable);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @BindingAdapter({"app:full_text", "app:span_text", "app:span_color", "app:span_font", "app:span_size"})
    public static void formatTextWithFont(TextView textView, String full_text, String span_text, int span_color, String span_font, float spanSize) {
        if (!TextUtils.isEmpty(span_text)) {
            int firstMatchingIndex = full_text.indexOf(span_text);
            int lastMatchingIndex = firstMatchingIndex + span_text.length();
            SpannableString spannable = new SpannableString(full_text);
            if (span_text.equalsIgnoreCase("Cancelled")) {
                span_color = textView.getContext().getColor(R.color.red);
            } else if (span_text.equalsIgnoreCase("Completed")) {
                span_color = textView.getContext().getColor(R.color.order_success_color);
            }
            spannable.setSpan(new ForegroundColorSpan(span_color), firstMatchingIndex, lastMatchingIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new RelativeSizeSpan(spanSize), firstMatchingIndex, lastMatchingIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new CustomTypefaceSpan(CustomFontFamily.getInstance().getFont(span_font)), firstMatchingIndex, lastMatchingIndex, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            textView.setText(spannable);
        } else {
            textView.setText(full_text);
        }
    }

    @BindingAdapter({"app:weight", "app:price"})
    public static void productMRP(TextView textView, String weight, String price) {
        String weightString = "MRP : " + weight + " - ";
        String priceString = "₹ " + price;
        SpannableString spannable = new SpannableString(weightString);
        spannable.setSpan(new CustomTypefaceSpan(CustomFontFamily.getInstance().getFont("robotoLight")), 0, weightString.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        SpannableString spannableString = new SpannableString(priceString);
        spannableString.setSpan(new CustomTypefaceSpan(CustomFontFamily.getInstance().getFont("robotoBold")), 0, priceString.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new StrikethroughSpan(), 0, priceString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(TextUtils.concat(spannable, " ", spannableString));
    }
}
