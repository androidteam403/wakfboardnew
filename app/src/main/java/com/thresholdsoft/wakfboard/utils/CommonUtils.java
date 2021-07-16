package com.thresholdsoft.wakfboard.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;

import com.thresholdsoft.wakfboard.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created on : March 30, 2020
 * Author     : JAGADEESH
 */
public class CommonUtils {
    private CommonUtils() {
        // This utility class is not publicly instantiable
    }

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    public static String dateConversion(String date){
        if(!TextUtils.isEmpty(date)) {
            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date newDate = null;
            try {
                newDate = spf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            spf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            if (newDate != null) {
                return spf.format(newDate);
            }
        }
        return "";
    }
    public static List<Integer> getColorList() {
        List<Integer> colorList = new ArrayList<>();
        colorList.add(R.color.yellow_transparent);
        colorList.add(R.color.red_transparent);
        colorList.add(R.color.green_transparent);
        colorList.add(R.color.blue_transparent);
        colorList.add(R.color.black_transparent);
        colorList.add(R.color.pink_transparent);
        return colorList;
    }
}
