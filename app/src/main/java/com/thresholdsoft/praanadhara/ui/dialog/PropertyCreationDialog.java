package com.thresholdsoft.praanadhara.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.databinding.DialogPolygoneBinding;
import com.thresholdsoft.praanadhara.databinding.SurveyPointDialogBinding;
import com.thresholdsoft.praanadhara.ui.surveytrack.SurveyTrackMvpView;

public class PropertyCreationDialog {
    private Dialog dialog;
    private DialogPolygoneBinding editQuantityDialogBinding;
    private SurveyTrackMvpView surveyTrackMvpView;

    private boolean negativeExist = false;

    public PropertyCreationDialog(Context context) {
        dialog = new Dialog(context);
        editQuantityDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_polygone, null, false);
        dialog.setContentView(editQuantityDialogBinding.getRoot());
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//        editQuantityDialogBinding.dialogButtonOK.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SurveyDetailsModel surveyDetailsModel = new SurveyDetailsModel();
//                surveyDetailsModel.setSurveyName(editQuantityDialogBinding.editNameEditText.getText().toString());
//                surveyTrackMvpView.onPassSurveyTrackEnteredDetails(surveyDetailsModel);
//
//            }
//        });
    }

    public void setEditTextDialogDetails(SurveyTrackMvpView surveyTrackMvpView) {
        this.surveyTrackMvpView = surveyTrackMvpView;
    }

    public void setPositiveListener(View.OnClickListener okListener) {
        editQuantityDialogBinding.dialogButtonOK.setOnClickListener(okListener);
    }

    public void setPositiveUploadImageListener(View.OnClickListener okListener) {
        editQuantityDialogBinding.uploadImage.setOnClickListener(okListener);
    }


    public void setNegativeListener(View.OnClickListener okListener) {
        editQuantityDialogBinding.dialogButtonNO.setOnClickListener(okListener);
    }

    public void show() {
        if (negativeExist) {
            editQuantityDialogBinding.dialogButtonNO.setVisibility(View.VISIBLE);
            editQuantityDialogBinding.separator.setVisibility(View.VISIBLE);
        } else {
            editQuantityDialogBinding.dialogButtonNO.setVisibility(View.GONE);
            editQuantityDialogBinding.separator.setVisibility(View.GONE);
        }
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public void setTitle(String title) {
        editQuantityDialogBinding.title.setText(title);
    }


    public void setPositiveLabel(String positive) {
        editQuantityDialogBinding.dialogButtonOK.setText(positive);
    }

    public void setNegativeLabel(String negative) {
        negativeExist = true;
        editQuantityDialogBinding.dialogButtonNO.setText(negative);
    }

    public String getPointName() {
        return editQuantityDialogBinding.editNameEditText.getText().toString();
    }

    public String getPointDescription() {
        return editQuantityDialogBinding.editDescriptionEditText.getText().toString();
    }
    public void setEditTextData(String editTextData) {
        editQuantityDialogBinding.editNameEditText.setText(editTextData);
    }

    public void setEditTextDescriptionData(String editTextDescriptionData) {
        editQuantityDialogBinding.editDescriptionEditText.setText(editTextDescriptionData);
    }

    public boolean validations() {
        String name = editQuantityDialogBinding.editNameEditText.getText().toString();
        String description = editQuantityDialogBinding.editDescriptionEditText.getText().toString();
        if (name.isEmpty()) {
            editQuantityDialogBinding.editNameEditText.setError("please enter name");
            editQuantityDialogBinding.editNameEditText.requestFocus();
            return false;
        }else if (description.isEmpty()){
            editQuantityDialogBinding.editDescriptionEditText.setError("Please enter description");
            editQuantityDialogBinding.editDescriptionEditText.requestFocus();
            return false;
        }
        return true;
    }
}
