package com.thresholdsoft.praanadhara.ui.mainactivity.fragments.newenrollmentfrag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.databinding.FragmentEnrollmentBinding;
import com.thresholdsoft.praanadhara.ui.base.BaseFragment;

import javax.inject.Inject;

public class NewEnrollmentFrag extends BaseFragment implements NewEnrollmentFragMvpView {
    @Inject
    NewEnrollmentFragMvpPresenter<NewEnrollmentFragMvpView> mpresenter;
    private FragmentEnrollmentBinding fragmentEnrollmentBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentEnrollmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        getActivityComponent().inject(this);
        mpresenter.onAttach(NewEnrollmentFrag.this);
        return fragmentEnrollmentBinding.getRoot();
    }

    @Override
    protected void setUp(View view) {

    }
}
