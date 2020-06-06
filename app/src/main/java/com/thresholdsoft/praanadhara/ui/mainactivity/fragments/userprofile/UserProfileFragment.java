package com.thresholdsoft.praanadhara.ui.mainactivity.fragments.userprofile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.databinding.FragmentDashboardBinding;
import com.thresholdsoft.praanadhara.ui.base.BaseFragment;

import javax.inject.Inject;

public class UserProfileFragment extends BaseFragment implements UserProfileMvpView {
    @Inject
    UserProfileMvpPresenter<UserProfileMvpView> mpresenter;
    private FragmentDashboardBinding fragmentDashboardBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentDashboardBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        getActivityComponent().inject(this);
        mpresenter.onAttach(UserProfileFragment.this);
        return fragmentDashboardBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUp(view);
    }

    @Override
    protected void setUp(View view) {

    }

    @Override
    public void anotherizedToken() {

    }
}