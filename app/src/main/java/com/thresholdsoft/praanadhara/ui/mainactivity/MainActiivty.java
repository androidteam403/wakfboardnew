package com.thresholdsoft.praanadhara.ui.mainactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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
import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.databinding.ActivityMainBinding;
import com.thresholdsoft.praanadhara.databinding.NavHeaderMainBinding;
import com.thresholdsoft.praanadhara.ui.base.BaseActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_customerMaster,
                R.id.nav_billing, R.id.nav_orders)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        getActivityComponent().inject(this);
        mPresenter.onAttach(MainActiivty.this);
        setUp();
    }

    @Override
    protected void setUp() {
//        View header = navigationView.getHeaderView(0);
//        TextView nav_header = (TextView) header.findViewById(R.id.storeIdnav);
//        TextView uName1 = (TextView) header.findViewById(R.id.userName1);
//        TextView uName2 = (TextView) header.findViewById(R.id.userName2);
//        nav_header.setText(mPresenter.getStoreId());
//        uName1.setText(mPresenter.getPharmaUser());
//        uName2.setText(mPresenter.getPharmaUser());
//        activityMainBinding.logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPresenter.clearSharedPref();
//                Intent intent = new Intent(MainActiivty.this, PharmaLoginActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.left_right, R.anim.right_left);
//            }
//        });

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.nav_orders:
//                Toast.makeText(this, "You clicked about", Toast.LENGTH_SHORT).show();
//              //  showBottomSheetDialogFragment();
//                break;
//
//
//        }
//        return true;
//    }

//    public void showBottomSheetDialogFragment() {
//        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
//        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
//        bottomSheetFragment.setDateDialogMvpView(this);
//
//    }

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
