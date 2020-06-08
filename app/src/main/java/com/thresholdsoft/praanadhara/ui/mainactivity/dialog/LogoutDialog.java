package com.thresholdsoft.praanadhara.ui.mainactivity.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.databinding.DialogLogotBinding;
import com.thresholdsoft.praanadhara.ui.base.BaseDialog;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.dialog.editdialog.EditDialogMvpPresenter;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.dialog.editdialog.EditDialogMvpView;
import com.thresholdsoft.praanadhara.ui.userlogin.UserLoginActivity;

import javax.inject.Inject;

public class LogoutDialog extends BaseDialog implements LogoutMvpView {
    @Inject
    LogoutMvpPresenter<LogoutMvpView> mpresenter;
    private DialogLogotBinding dialogLogotBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialogLogotBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_logot, container, false);
        getActivityComponent().inject(this);
        mpresenter.onAttach(LogoutDialog.this);
        // init ViewModel
        return dialogLogotBinding.getRoot();
    }

    @Override
    protected void setUp(View view) {
        dialogLogotBinding.setCallbacks(mpresenter);
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
        mpresenter.clearSharedPreference();
        Intent intent = new Intent(getContext(), UserLoginActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}
