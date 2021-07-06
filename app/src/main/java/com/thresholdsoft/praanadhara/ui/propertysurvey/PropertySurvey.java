package com.thresholdsoft.praanadhara.ui.propertysurvey;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.databinding.ActivityPropertySurveyBinding;
import com.thresholdsoft.praanadhara.ui.base.BaseActivity;
import com.thresholdsoft.praanadhara.ui.dialog.PropertyCreationDialog;
import com.thresholdsoft.praanadhara.ui.photouploadactivity.PhotoUpload;
import com.thresholdsoft.praanadhara.ui.propertysurvey.model.PointDataTable;
import com.thresholdsoft.praanadhara.ui.propertysurvey.model.PolylineDataTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PropertySurvey extends BaseActivity implements PropertySurveyMvpView, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    @Inject
    PropertySurveyMvpPresenter<PropertySurveyMvpView> mpresenter;
    ActivityPropertySurveyBinding propertySurveyBinding;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    Location currentLocation;
    private GoogleMap mMap;
    private static final int PHOTO_UPLOAD = 141;


    Polyline polyline = null;
    List<Polyline> polylineList = new ArrayList<>();
    List<LatLng> latLngList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();
    Marker polyLineMarker;
    private int mapTypeData;

    public static Intent getStartIntent(Context context, int mapType) {
        Intent intent = new Intent(context, PropertySurvey.class);
        intent.putExtra("maptype", mapType);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        propertySurveyBinding = DataBindingUtil.setContentView(this, R.layout.activity_property_survey);
        getActivityComponent().inject(this);
        mpresenter.onAttach(PropertySurvey.this);
        setUp();
    }

    @Override
    protected void setUp() {

        propertySurveyBinding.setCallbacks(mpresenter);

        if (getIntent() != null) {
            mapTypeData = (int) getIntent().getIntExtra("maptype", 0);
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();

        if (mapTypeData == 1) {
            propertySurveyBinding.polygonStart.setVisibility(View.GONE);
            propertySurveyBinding.polygonSave.setVisibility(View.GONE);
            propertySurveyBinding.polygonStop.setVisibility(View.GONE);
            propertySurveyBinding.polygonLay.setVisibility(View.GONE);
            propertySurveyBinding.polylineLay.setVisibility(View.GONE);
            propertySurveyBinding.pointSave.setVisibility(View.VISIBLE);
            propertySurveyBinding.selectedMap.setVisibility(View.VISIBLE);
            propertySurveyBinding.typeTextview.setBackgroundResource(R.drawable.new_point);
            propertySurveyBinding.selectedMap.setText("Selected Map Type : Ponit");
        } else if (mapTypeData == 2) {
            propertySurveyBinding.polygonStart.setVisibility(View.GONE);
            propertySurveyBinding.polygonSave.setVisibility(View.GONE);
            propertySurveyBinding.polygonStop.setVisibility(View.GONE);
            propertySurveyBinding.polygonLay.setVisibility(View.GONE);
            propertySurveyBinding.pointSave.setVisibility(View.GONE);
            propertySurveyBinding.polylineLay.setVisibility(View.VISIBLE);
            propertySurveyBinding.selectedMap.setVisibility(View.VISIBLE);
            propertySurveyBinding.typeTextview.setBackgroundResource(R.drawable.new_line);
            propertySurveyBinding.selectedMap.setText("Selected Map Type : PolyLine");
        } else if (mapTypeData == 3) {
            propertySurveyBinding.polygonStart.setVisibility(View.VISIBLE);
            propertySurveyBinding.polylineLay.setVisibility(View.GONE);
            propertySurveyBinding.pointSave.setVisibility(View.GONE);
            propertySurveyBinding.selectedMap.setVisibility(View.VISIBLE);
            propertySurveyBinding.typeTextview.setBackgroundResource(R.drawable.new_polygon);
            propertySurveyBinding.selectedMap.setText("Selected Map Type : Polygon");
        }
        propertySurveyBinding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
//                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();

                    Toast toast = Toast.makeText(PropertySurvey.this, currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT);
                    toast.getView().setBackground(getResources().getDrawable(R.drawable.toast_bg));
                    TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Typeface typeface = Typeface.createFromAsset(getApplication().getAssets(), "font/roboto_bold.ttf");
                        text.setTypeface(typeface);
                        text.setTextColor(Color.WHITE);
                        text.setTextSize(14);
                    }
                    toast.show();

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(PropertySurvey.this);
                }
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.blue_circle);

        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!").icon(icon);
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));
        googleMap.addMarker(markerOptions);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                if (mapTypeData == 0) {
//                    Toast.makeText(PropertySurvey.this, "Please Select MapType", Toast.LENGTH_SHORT).show();
                    getSnackBarView("Please Select MapType");
                } else if (mapTypeData == 1) {
                    if (markerList.size() < 1) {
                        addPoint(latLng);
                        mMap.setOnMarkerClickListener(PropertySurvey.this);
                    }
                } else if (mapTypeData == 2) {

                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).draggable(true);
                    polyLineMarker = mMap.addMarker(markerOptions);
                    latLngList.add(latLng);
                    markerList.add(polyLineMarker);

                    if (polyline != null) polyline.remove();
                    PolylineOptions polylineOptions = new PolylineOptions().addAll(latLngList).color(Color.BLUE).width(10).clickable(true);
                    polyline = mMap.addPolyline(polylineOptions);
                    polylineList.add(polyline);

                    for (LatLng latLngPol : latLngList) {

                        int position = latLngList.indexOf(latLngPol);
                        MarkerTag yourMarkerTag = new MarkerTag();
                        yourMarkerTag.setLatLng(latLngPol);
                        yourMarkerTag.setPosition(position);
                        polyLineMarker.setTag(yourMarkerTag);

                        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                            @Override
                            public void onMarkerDragStart(Marker marker) {

                            }

                            @Override
                            public void onMarkerDrag(Marker marker) {
                            }

                            @Override
                            public void onMarkerDragEnd(Marker marker) {
                                updateMarkerLocation(marker);
                            }
                        });
                    }

                    mMap.setOnMarkerClickListener(PropertySurvey.this);
                }
            }
        });
    }


    private void updateMarkerLocation(Marker marker) {
        MarkerTag tag = (MarkerTag) marker.getTag();
        assert tag != null;
        int position = latLngList.indexOf(tag.getLatLng());
        if (position != -1) {
            latLngList.set(position, marker.getPosition());
            MarkerTag markerTag = new MarkerTag();
            markerTag.setPosition(position);
            markerTag.setLatLng(marker.getPosition());
            marker.setTag(markerTag);
        } else {
            marker.remove();
        }

        if (polyline != null) polyline.remove();
        PolylineOptions polylineOptions = new PolylineOptions().addAll(latLngList).color(Color.BLUE).width(10).clickable(true);
        polyline = mMap.addPolyline(polylineOptions);
        polylineList.add(polyline);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker != null) {
            if (mapTypeData == 1) {
                marker.remove();
                markerList.remove(marker);
            }
        }
        return true;
    }

    private static class MarkerTag implements Serializable {
        private LatLng latLng;
        private int position;

        public LatLng getLatLng() {
            return latLng;
        }

        public void setLatLng(LatLng latLng) {
            this.latLng = latLng;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    private void addPoint(LatLng latLng) {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.new_point);
        polyLineMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .flat(true).icon(icon)
                .anchor(0.5f, 0.5f).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                polyLineMarker = marker;
                markerList.clear();
                markerList.add(polyLineMarker);
            }
        });
        markerList.add(polyLineMarker);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }


    @Override
    public void anotherizedToken() {

    }

    @Override
    public void getSnackBarView(String msg) {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        Snackbar snackbar = Snackbar
                .make(relativeLayout, msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        Typeface font = Typeface.createFromAsset(getApplication().getAssets(), "font/roboto_bold.ttf");
        TextView textView = (TextView) snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTypeface(font);
        snackBarView.setBackgroundColor(this.getResources().getColor(R.color.red));
        snackbar.show();
    }

    @Override
    public void polygonStartClick() {
        propertySurveyBinding.polygonLay.setVisibility(View.VISIBLE);
    }

    @Override
    public void polygonSaveClick() {

    }

    @Override
    public void polygonPauseClick() {
        propertySurveyBinding.polygonResume.setVisibility(View.VISIBLE);
        propertySurveyBinding.polygonPause.setVisibility(View.GONE);
    }

    @Override
    public void polygonResumeClick() {
        propertySurveyBinding.polygonResume.setVisibility(View.GONE);
        propertySurveyBinding.polygonPause.setVisibility(View.VISIBLE);
    }

    @Override
    public void polygonStopClick() {
        propertySurveyBinding.polygonLay.setVisibility(View.GONE);
        propertySurveyBinding.polygonSave.setVisibility(View.VISIBLE);
    }

    @Override
    public void polylineUndoClick() {
        if (markerList.size() > 0) {

            latLngList.remove(latLngList.size() - 1);


            Marker marker1 = markerList.get(markerList.size() - 1);
            marker1.remove();
            markerList.remove(markerList.size() - 1);

            for (int i = 0; i < polylineList.size(); i++) {
                if (i == polylineList.size() - 1) {
                    polylineList.remove(polylineList.size() - 1);
                    polylineList.remove(polyline);
                    if (polyline != null) polyline.remove();
                    PolylineOptions polylineOptions = new PolylineOptions().addAll(latLngList).color(Color.BLUE).width(10).clickable(true);
                    polyline = mMap.addPolyline(polylineOptions);
                    i--;
                }
            }
        }
    }

    @Override
    public void polylineClearClick() {
        if (polyline != null) polyline.remove();
        for (Marker marker : markerList)
            marker.remove();
        latLngList.clear();
        markerList.clear();
    }

    @Override
    public void polylineSaveClick() {
        if (polylineList.size() < 2) {

            Toast toast = Toast.makeText(PropertySurvey.this, "Please select a polyline on map", Toast.LENGTH_SHORT);
            toast.getView().setBackground(getResources().getDrawable(R.drawable.toast_bg));
            TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Typeface typeface = Typeface.createFromAsset(getApplication().getAssets(), "font/roboto_bold.ttf");
                text.setTypeface(typeface);
                text.setTextColor(Color.WHITE);
                text.setTextSize(14);
            }
            toast.show();

        } else {
            showPolylineDialog();
        }
    }

    @Override
    public void pointSaveClick() {
        if (markerList.size() < 1) {
//                    Toast.makeText(PropertySurvey.this, "Please select a point on map", Toast.LENGTH_SHORT).show();

            Toast toast = Toast.makeText(PropertySurvey.this, "Please select a point on map", Toast.LENGTH_SHORT);
            toast.getView().setBackground(getResources().getDrawable(R.drawable.toast_bg));
            TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Typeface typeface = Typeface.createFromAsset(getApplication().getAssets(), "font/roboto_bold.ttf");
                text.setTypeface(typeface);
                text.setTextColor(Color.WHITE);
                text.setTextSize(14);
            }
            toast.show();

        } else {
            LatLng latLng = new LatLng(markerList.get(0).getPosition().latitude, markerList.get(0).getPosition().longitude);
            showPointDialog(latLng);
        }
    }

    private void showPolylineDialog() {
        PropertyCreationDialog dialogView = new PropertyCreationDialog(this);
        dialogView.setTitle("Polyline Details");
        dialogView.setPositiveLabel("Ok");
        dialogView.setPositiveListener(view -> {
            if (dialogView.validations()) {
                dialogView.dismiss();
                if (latLngList != null && latLngList.size() > 0) {
                    if (latLngList.size() == markerList.size()) {
                        for (int i = 0; i < latLngList.size(); i++) {
                            PolylineDataTable polylineDataTable = new PolylineDataTable(latLngList.get(i).latitude, latLngList.get(i).longitude, dialogView.getPointName(), dialogView.getPointDescription(), imagesUploadedList);
                            mpresenter.insertPolyLineData(polylineDataTable);

                        }
                    }
                }
                Toast toast = Toast.makeText(PropertySurvey.this, "Polyline Details are saved successfully", Toast.LENGTH_SHORT);
                toast.getView().setBackground(getResources().getDrawable(R.drawable.toast_bg));
                TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Typeface typeface = Typeface.createFromAsset(getApplication().getAssets(), "font/roboto_bold.ttf");
                    text.setTypeface(typeface);
                    text.setTextColor(Color.WHITE);
                    text.setTextSize(14);
                }
                toast.show();


            }
        });
        dialogView.setPositiveUploadImageListener(view -> {
            startActivityForResult(PhotoUpload.getStartIntent(PropertySurvey.this), PHOTO_UPLOAD);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        });
        dialogView.setNegativeLabel("Cancel");
        dialogView.setNegativeListener(v -> {
            dialogView.dismiss();
//            Toast.makeText(PropertySurvey.this, "PointDetails are not saved", Toast.LENGTH_LONG).show();
            Toast toast = Toast.makeText(PropertySurvey.this, "Polyline Details are not saved", Toast.LENGTH_SHORT);
            toast.getView().setBackground(getResources().getDrawable(R.drawable.toast_bg));
            TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Typeface typeface = Typeface.createFromAsset(getApplication().getAssets(), "font/roboto_bold.ttf");
                text.setTypeface(typeface);
                text.setTextColor(Color.WHITE);
                text.setTextSize(14);
            }
            toast.show();
        });
        dialogView.show();
    }

    private void showPointDialog(LatLng latLng) {
        PropertyCreationDialog dialogView = new PropertyCreationDialog(this);
        dialogView.setTitle("Point Details");
        dialogView.setPositiveLabel("Ok");
        dialogView.setPositiveListener(view -> {
            if (dialogView.validations()) {
                dialogView.dismiss();
                PointDataTable pointDataTable = new PointDataTable(latLng.latitude, latLng.longitude, dialogView.getPointName(), dialogView.getPointDescription(), imagesUploadedList);
                mpresenter.insertPointDataTable(pointDataTable);
//                Toast.makeText(PropertySurvey.this, "PointDetails are saved successfully", Toast.LENGTH_LONG).show();
                Toast toast = Toast.makeText(PropertySurvey.this, "Point Details are saved successfully", Toast.LENGTH_SHORT);
                toast.getView().setBackground(getResources().getDrawable(R.drawable.toast_bg));
                TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Typeface typeface = Typeface.createFromAsset(getApplication().getAssets(), "font/roboto_bold.ttf");
                    text.setTypeface(typeface);
                    text.setTextColor(Color.WHITE);
                    text.setTextSize(14);
                }
                toast.show();
            }
        });
        dialogView.setPositiveUploadImageListener(view -> {
            startActivityForResult(PhotoUpload.getStartIntent(PropertySurvey.this), PHOTO_UPLOAD);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        });
        dialogView.setNegativeLabel("Cancel");
        dialogView.setNegativeListener(v -> {
            dialogView.dismiss();
//            Toast.makeText(PropertySurvey.this, "PointDetails are not saved", Toast.LENGTH_LONG).show();
            Toast toast = Toast.makeText(PropertySurvey.this, "PointDetails are not saved", Toast.LENGTH_SHORT);
            toast.getView().setBackground(getResources().getDrawable(R.drawable.toast_bg));
            TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Typeface typeface = Typeface.createFromAsset(getApplication().getAssets(), "font/roboto_bold.ttf");
                text.setTypeface(typeface);
                text.setTextColor(Color.WHITE);
                text.setTextSize(14);
            }
            toast.show();
        });
        dialogView.show();
    }

    List<String> imagesUploadedList = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_UPLOAD:
                    if (data != null) {
                        List<String> imageStringListPaths = (List<String>) data.getSerializableExtra("mpaths");
                        imagesUploadedList = imageStringListPaths;
                    }
                    break;
                default:
            }
        }

    }


}
