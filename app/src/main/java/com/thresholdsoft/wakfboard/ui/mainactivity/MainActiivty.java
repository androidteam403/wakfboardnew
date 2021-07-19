package com.thresholdsoft.wakfboard.ui.mainactivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.thresholdsoft.wakfboard.R;
import com.thresholdsoft.wakfboard.databinding.ActivityMainBinding;
import com.thresholdsoft.wakfboard.databinding.NavHeaderMainBinding;
import com.thresholdsoft.wakfboard.databinding.ToolbarBinding;
import com.thresholdsoft.wakfboard.ui.base.BaseActivity;
import com.thresholdsoft.wakfboard.ui.mainactivity.dialog.LogoutDialog;
import com.thresholdsoft.wakfboard.ui.userlogin.UserLoginActivity;

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
    TextView count, tittle;
    DrawerLayout drawer;
    ToolbarBinding toolbarBinding;
    private ImageView syncImage, propertyIcon;
    TextView logout;
    private boolean doubleBackToExitPressedOnce = false;


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

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        activityMainBinding.setLogout(mPresenter);

        ImageView imageView = findViewById(R.id.menulines);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        syncImage = findViewById(R.id.refresh_sync);
        propertyIcon = findViewById(R.id.property_icon);
        tittle = findViewById(R.id.tittle);
        syncImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation rotation = AnimationUtils.loadAnimation(MainActiivty.this, R.anim.rotate_refresh);
                rotation.setRepeatCount(Animation.INFINITE);
                syncImage.startAnimation(rotation);
                mPresenter.syncData();
            }
        });

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);
        getActivityComponent().inject(this);
        mPresenter.onAttach(MainActiivty.this);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (mAppBarConfiguration.getDrawerLayout() != null) {
                    mAppBarConfiguration.getDrawerLayout().closeDrawers();
                }
                if (menuItem.isChecked()) return false;

                if (menuItem.getItemId() == R.id.nav_home) {
                    propertyIcon.setVisibility(View.VISIBLE);
                    navController.navigate(R.id.nav_home);
                } else if (menuItem.getItemId() == R.id.nav_profile) {
                    propertyIcon.setVisibility(View.GONE);
                    navController.navigate(R.id.nav_profile);
                } else if (menuItem.getItemId() == R.id.nav_enrollment) {
                    propertyIcon.setVisibility(View.GONE);
                    navController.navigate(R.id.nav_enrollment);
                }
                return true;
            }
        });
        setUp();
    }

    public void setActionBarTitle(String title) {
        TextView tittleName = findViewById(R.id.tittle);
        tittleName.setText(title);
    }

    @Override
    protected void setUp() {
        TextView userName = activityMainBinding.navView.getHeaderView(0).findViewById(R.id.user_name);
        userName.setText(mPresenter.getUserName());

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawers();
                LogoutDialog logoutDialog = new LogoutDialog();
                logoutDialog.show(getSupportFragmentManager(), "logout");
                Toast.makeText(getApplicationContext(), "logout", Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    public void syncComplete() {
        if (syncImage != null) {
            syncImage.clearAnimation();
            showMessage("Sync Completed successfully");
        }
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
//        if (Navigation.findNavController(this, R.id.nav_host_fragment)
//                .getCurrentDestination().getId() == R.id.nav_profile) {
//            syncImage.setVisibility(View.VISIBLE);
//        }
//        super.onBackPressed();
    }
}
