package com.thresholdsoft.praanadhara.ui.mainactivity.fragments.userprofilefrag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.databinding.FragmentDashboardBinding;
import com.thresholdsoft.praanadhara.databinding.ToolbarBinding;
import com.thresholdsoft.praanadhara.ui.base.BaseFragment;
import com.thresholdsoft.praanadhara.ui.mainactivity.MainActiivty;

import javax.inject.Inject;

public class UserProfileFragment extends BaseFragment implements UserProfileMvpView {
    @Inject
    UserProfileMvpPresenter<UserProfileMvpView> mpresenter;
    private FragmentDashboardBinding fragmentDashboardBinding;
    ToolbarBinding toolbarBinding;


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
        fragmentDashboardBinding.setPresenter(mpresenter);
        fragmentDashboardBinding.firstName.setText(mpresenter.getUserName());
        fragmentDashboardBinding.email.setText(mpresenter.getUserEmail());
        fragmentDashboardBinding.contact.setText(mpresenter.getUserContactNum());

        ((MainActiivty) getActivity()).setActionBarTitle("Profile");
    }

    @Override
    public void anotherizedToken() {

    }

    private boolean validations() {
        String fname = fragmentDashboardBinding.firstName.getText().toString().trim();
        String lname = fragmentDashboardBinding.lastname.getText().toString().trim();
        if (fname.isEmpty()) {
            fragmentDashboardBinding.firstName.setError("Please enter First Name");
            fragmentDashboardBinding.firstName.requestFocus();
            return false;
        } else if (lname.isEmpty()) {
            fragmentDashboardBinding.lastname.setError("Please enter Last Name");
            fragmentDashboardBinding.lastname.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onUpdateClick() {
        if (validations()) {
            Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCancelClick() {
        Toast.makeText(getContext(), "Canceled", Toast.LENGTH_SHORT).show();
    }

}