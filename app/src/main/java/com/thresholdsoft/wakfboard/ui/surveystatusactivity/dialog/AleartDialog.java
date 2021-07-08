package com.thresholdsoft.wakfboard.ui.surveystatusactivity.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.thresholdsoft.wakfboard.R;
import com.thresholdsoft.wakfboard.databinding.AleartInfoDialogBinding;

public class AleartDialog {

    private Dialog dialog;
    private AleartInfoDialogBinding exitInfoDialogBinding;

    private boolean negativeExist = false;

    public AleartDialog(Context context) {
        dialog = new Dialog(context);
        exitInfoDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.aleart_info_dialog, null, false);
        dialog.setContentView(exitInfoDialogBinding.getRoot());
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


    public void setPositiveListener(View.OnClickListener okListener) {
        exitInfoDialogBinding.dialogButtonOK.setOnClickListener(okListener);
    }

    public void setNegativeListener(View.OnClickListener okListener) {
        exitInfoDialogBinding.dialogButtonNO.setOnClickListener(okListener);
    }

    public boolean isDisplay(){
      return   dialog.isShowing();
    }


    public void show() {
        if (negativeExist) {
            exitInfoDialogBinding.dialogButtonNO.setVisibility(View.VISIBLE);
            exitInfoDialogBinding.separator.setVisibility(View.VISIBLE);
        } else {
            exitInfoDialogBinding.dialogButtonNO.setVisibility(View.GONE);
            exitInfoDialogBinding.separator.setVisibility(View.GONE);
        }
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public void setTitle(String title) {
        exitInfoDialogBinding.title.setText(title);
    }

    public void setSubtitle(String subtitle) {
        exitInfoDialogBinding.subtitle.setText(subtitle);
    }

    public void setPositiveLabel(String positive) {
        exitInfoDialogBinding.dialogButtonOK.setText(positive);
    }

    public void setNegativeLabel(String negative) {
        negativeExist = true;
        exitInfoDialogBinding.dialogButtonNO.setText(negative);
    }

    public void setDialogDismiss(){
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
//        dialog.setOnKeyListener(new Dialog.OnKeyListener() {
//
//            @Override
//            public boolean onKey(DialogInterface arg0, int keyCode,
//                                 KeyEvent event) {
//                // TODO Auto-generated method stub
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//
//                }
//                return true;
//            }
//        });
    }
}
