package com.thresholdsoft.praanadhara.ui.mainactivity.fragments.dasboardfrag;

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

public class DashboardFragment extends BaseFragment implements DashboardMvpView {
    @Inject
    DashboardMvpPresenter<DashboardMvpView> mpresenter;
    private FragmentDashboardBinding fragmentDashboardBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentDashboardBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        getActivityComponent().inject(this);
        mpresenter.onAttach(DashboardFragment.this);
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
}