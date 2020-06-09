package com.thresholdsoft.praanadhara.ui.mainactivity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.databinding.ActivityMainBinding;
import com.thresholdsoft.praanadhara.databinding.NavHeaderMainBinding;
import com.thresholdsoft.praanadhara.ui.base.BaseActivity;
import com.thresholdsoft.praanadhara.ui.mainactivity.dialog.LogoutDialog;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.newenrollmentfrag.NewEnrollmentFrag;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.SurveyListFrag;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.userprofilefrag.UserProfileFragment;
import com.thresholdsoft.praanadhara.ui.userlogin.UserLoginActivity;

import javax.inject.Inject;

public class MainActiivty extends BaseActivity implements MainActivityMvpView {

    @Inject
    MainActivityMvpPresenter<MainActivityMvpView> mPresenter;
    MainActivityMvpView mainActivityMvpView;
    private AppBarConfiguration mAppBarConfiguration;
    ActivityMainBinding activityMainBinding;
    NavHeaderMainBinding navHeaderMainBinding;
    NavigationView navigationView;
    Toolbar mTopToolbar;
    TextView count;
    DrawerLayout drawer;
    Fragment fragment ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_enrollment)
                .setDrawerLayout(drawer)
                .build();

        ImageView imageView = findViewById(R.id.menulines);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);
        getActivityComponent().inject(this);
        mPresenter.onAttach(MainActiivty.this);
        setUp();
    }

//    public void setTitle(final String title) {
//        ((TextView) findViewById(R.id.name)).setText(title);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        String title = null;
//        switch (item.getItemId()) {
//            case 0:
//                fragment = new SurveyListFrag();
//                title = "home";
//                break;
//            case 1:
//                fragment = new NewEnrollmentFrag();
//                title = "enrollment";
//                break;
//            case 2:
//                fragment = new UserProfileFragment();
//                title = "User Profile";
//                break;
//        }
//        if (fragment != null){
//
//            FragmentManager fragmentManager = getFragmentManager();
//
//            //The key is this line
//            if (title != null && findViewById(R.id.name)!= null ) setTitle(title);
//        }
//        return true;
//    }

    @Override
    protected void setUp() {
        activityMainBinding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                LogoutDialog dialog = new LogoutDialog();
                dialog.show(getSupportFragmentManager(), "logoutdialog");

            }
        });
        TextView userName = activityMainBinding.navView.getHeaderView(0).findViewById(R.id.user_name);
        userName.setText(mPresenter.getUserName());


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void anotherizedToken() {
        Intent intent = new Intent(this, UserLoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
