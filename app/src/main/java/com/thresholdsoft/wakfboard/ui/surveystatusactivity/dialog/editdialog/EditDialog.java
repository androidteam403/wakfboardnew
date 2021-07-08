package com.thresholdsoft.wakfboard.ui.surveystatusactivity.dialog.editdialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.thresholdsoft.wakfboard.R;
import com.thresholdsoft.wakfboard.databinding.DialogEditBinding;
import com.thresholdsoft.wakfboard.ui.base.BaseDialog;

import javax.inject.Inject;

public class EditDialog extends BaseDialog implements EditDialogMvpView {
    @Inject
    EditDialogMvpPresenter<EditDialogMvpView> mpresenter;
    private DialogEditBinding dialogEditBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialogEditBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_edit, container, false);
        getActivityComponent().inject(this);
        mpresenter.onAttach(EditDialog.this);
        // init ViewModel
        return dialogEditBinding.getRoot();
    }

    @Override
    protected void setUp(View view) {
//        dialogEditBinding.setCallbacks(mpresenter);
    }

    @Override
    public void openLoginActivity() {

    }

    @Override
    public void anotherizedToken() {

    }

    @Override
    public void onNoClick() {
        dismiss();
    }

    @Override
    public void onYesClick() {
//        mpresenter.clearSharedPreference();
//        Intent intent = new Intent(getContext(), UserLoginActivity.class);
//        startActivity(intent);
//        getActivity().overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}
