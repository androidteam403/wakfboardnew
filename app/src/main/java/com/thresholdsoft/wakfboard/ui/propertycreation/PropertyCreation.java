package com.thresholdsoft.wakfboard.ui.propertycreation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thresholdsoft.wakfboard.R;
import com.thresholdsoft.wakfboard.databinding.ActivityPropertyCreationBinding;
import com.thresholdsoft.wakfboard.ui.alertdialog.CutomAlertBox;
import com.thresholdsoft.wakfboard.ui.base.BaseActivity;
import com.thresholdsoft.wakfboard.ui.propertycreation.adapter.PhotosUploadAdapter;
import com.thresholdsoft.wakfboard.ui.propertycreation.model.PropertyData;
import com.thresholdsoft.wakfboard.ui.propertysurveystatus.PropertyPreview;
import com.thresholdsoft.wakfboard.utils.CommonUtils;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import static android.text.TextUtils.isEmpty;

@RequiresApi(api = Build.VERSION_CODES.M)
public class PropertyCreation extends BaseActivity implements PropertyMvpView {
    @Inject
    PropertyMvpPresenter<PropertyMvpView> mpresenter;
    ActivityPropertyCreationBinding propertyCreationBinding;
    private int PICK_IMAGES = 1;
    private PhotosUploadAdapter photosUploadAdapter;
    private PropertyData propertyData;
    private boolean isUpdateScreen = false;

    public static final String PROPERTY_DATA_KEY = "PROPERTY_DATA_KEY";
    public static final String IS_UPDATE_SCREEN = "IS_UPDATE_SCREEN";
    public static final int ACTIVITY_ID = 188;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, PropertyCreation.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;
    }

    public static Intent getStartIntent(Context context, PropertyData propertyData, boolean isUpdateScreen) {
        Intent intent = new Intent(context, PropertyCreation.class);
        intent.putExtra(PROPERTY_DATA_KEY, propertyData);
        intent.putExtra(IS_UPDATE_SCREEN, isUpdateScreen);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        propertyCreationBinding = DataBindingUtil.setContentView(this, R.layout.activity_property_creation);
        getActivityComponent().inject(this);
        mpresenter.onAttach(PropertyCreation.this);
        setUp();
    }

    @Override
    protected void setUp() {
        propertyCreationBinding.setCallback(mpresenter);
        getAddAddressTypes();
        getStateList();
        getPropertryList();
        if (getIntent() != null) {
            propertyData = (PropertyData) getIntent().getSerializableExtra(PROPERTY_DATA_KEY);
            if (propertyData != null) {
                isUpdateScreen = getIntent().getBooleanExtra(IS_UPDATE_SCREEN, false);
                propertyCreationBinding.tittle.setText(R.string.label_edit_property_details);
                propertyCreationBinding.save.setText(R.string.label_update);
                updateProperyData(propertyData);
            }
        }
        propertyCreationBinding.upload.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View v) {
                new ImagePicker.Builder(PropertyCreation.this)
                        .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                        .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                        .directory(ImagePicker.Directory.DEFAULT)
                        .extension(ImagePicker.Extension.PNG)
                        .scale(600, 600)
                        .allowMultipleImages(true)
                        .enableDebuggingMode(true)
                        .build();
            }
        });

        propertyCreationBinding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                    Date todayDate = new Date();
                    String thisDate = currentDate.format(todayDate);
                    PropertyData propertyData1 = new PropertyData(propertyCreationBinding.propertyName.getText().toString(),
                            propertyCreationBinding.propertyType.getSelectedItem().toString(),
                            propertyCreationBinding.propertyValue.getText().toString(),
                            propertyCreationBinding.village.getText().toString(),
                            propertyCreationBinding.mandal.getText().toString(),
                            propertyCreationBinding.state.getSelectedItem().toString(),
                            propertyCreationBinding.district.getText().toString(),
                            propertyCreationBinding.areaType.getSelectedItem().toString(), mPaths,
                            propertyCreationBinding.mobile.getText().toString(), thisDate);
                    if (propertyData != null) {
                        propertyData1.setId(propertyData.getId());
                        propertyData = propertyData1;
                    } else {
                        propertyData = propertyData1;
                    }
                    mpresenter.insertPropertyData(propertyData1);
                    getLocationPermmision(propertyData1);


                }
            }
        });
        propertyCreationBinding.propertyValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                propertyCreationBinding.propertyValue.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();
                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    propertyCreationBinding.propertyValue.setText(formattedString);
                    propertyCreationBinding.propertyValue.setSelection(propertyCreationBinding.propertyValue.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                propertyCreationBinding.propertyValue.addTextChangedListener(this);
            }
        });

    }

    private void updateProperyData(PropertyData propertyData) {
        if (propertyData != null) {
            propertyCreationBinding.propertyName.setText(propertyData.getPropertyName());
            propertyCreationBinding.mobile.setText(propertyData.getMobileNumber());
            for (int i = 0; i < getPropertyTypeListData().size(); i++) {
                if (getPropertyTypeListData().get(i).getPropertyType().equals(propertyData.getPropertyType())) {
                    propertyCreationBinding.propertyType.setSelection(i);
                    break;
                }
            }
            propertyCreationBinding.propertyValue.setText(String.valueOf(propertyData.getPropertyValue()));
            propertyCreationBinding.village.setText(propertyData.getVillage());
            propertyCreationBinding.mandal.setText(propertyData.getMandal());
            propertyCreationBinding.district.setText(propertyData.getDistrict());
            for (int i = 0; i < getStateListData().size(); i++) {
                if (getStateListData().get(i).getState().equals(propertyData.getState())) {
                    propertyCreationBinding.state.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < getAddressTypeListData().size(); i++) {
                if (getAddressTypeListData().get(i).getAreaType().equals(propertyData.getMeasuredunit())) {
                    propertyCreationBinding.areaType.setSelection(i);
                    break;
                }
            }
            if (propertyData.getPhotosList() != null && propertyData.getPhotosList().size() > 0) {
                this.mPaths = propertyData.getPhotosList();
                photosUploadAdapter = new PhotosUploadAdapter(this, mPaths, this);
                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this);
                propertyCreationBinding.photoRecycles.setLayoutManager(new GridLayoutManager(this, 3));
                propertyCreationBinding.photoRecycles.setAdapter(photosUploadAdapter);
            }
        }
    }

    private static final int REQUEST_PERMISSION_LOCATION = 255; // int should be between 0 and 255
    private PropertyData decideNextScreenPropertyData;

    private void getLocationPermmision(PropertyData propertyData1) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_LOCATION);
        } else {
//            this.decideNextScreenPropertyData = propertyData1;
//            gpsAccess();
            decideNextScreen(propertyData1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We now have permission to use the location
//                this.decideNextScreenPropertyData = propertyData;
//                gpsAccess();
                decideNextScreen(propertyData);
            } else {
                finish();
            }
        }
    }

    private void gpsAccess() {
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled) {
            // notify user
            new AlertDialog.Builder(this)
                    .setMessage(R.string.gps_network_not_enabled)
                    .setCancelable(false)
                    .setPositiveButton(R.string.open_location_settings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), CommonUtils.LOACTION_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {
            decideNextScreen(decideNextScreenPropertyData);
        }


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void decideNextScreen(PropertyData pd) {
        Intent intent;
        if (!isUpdateScreen)
            intent = new Intent(PropertyCreation.this, PropertyPreview.class);
        else
            intent = new Intent();

        intent.putExtra("id", mpresenter.propertyID());
        intent.putExtra("measurements", propertyCreationBinding.areaType.getSelectedItem().toString());
        intent.putExtra("propertyName", propertyCreationBinding.propertyName.getText().toString());
        intent.putExtra("village", propertyCreationBinding.propertyName.getText().toString());
        if (pd.getId() == 0) {
            pd.setId(mpresenter.propertyID());
        }
        intent.putExtra(PROPERTY_DATA_KEY, pd);
        if (isUpdateScreen) {
            Toast.makeText(PropertyCreation.this, "Property details are updated", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK, intent);
        } else {
            Toast.makeText(PropertyCreation.this, "Property details are saved", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }

    private List<String> mPaths = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonUtils.LOACTION_REQUEST_CODE) {
            gpsAccess();
        }
        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPathsDummy = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
            if (mPaths != null) {
                if (mPathsDummy != null && mPathsDummy.size() > 0) {
                    for (String path : mPathsDummy) {
                        mPaths.add(path);
                    }
                }
            } else {
                mPaths = new ArrayList<>();
                if (mPathsDummy != null && mPathsDummy.size() > 0) {
                    for (String path : mPathsDummy) {
                        mPaths.add(path);
                    }
                }
            }

            //Your Code

            photosUploadAdapter = new PhotosUploadAdapter(this, mPaths, this);
            RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this);
            propertyCreationBinding.photoRecycles.setLayoutManager(new GridLayoutManager(this, 3));
            propertyCreationBinding.photoRecycles.setAdapter(photosUploadAdapter);


        }
    }


    public void getAddAddressTypes() {
        propertyCreationBinding.areaType.getEditText().setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "font/roboto_regular.ttf"));
        propertyCreationBinding.areaType.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "font/roboto_regular.ttf"));
        ArrayAdapter<AreaModel> addresModelArrayAdapter = new ArrayAdapter<AreaModel>(getApplicationContext(), android.R.layout.simple_spinner_item, getAddressTypeListData()) {
            @NotNull
            public View getView(int position, View convertView, @NotNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                Typeface externalFont = Typeface.createFromAsset(getContext().getAssets(), "font/roboto_regular.ttf");
                ((TextView) v).setTypeface(externalFont);
                return v;
            }

            public View getDropDownView(int position, View convertView, @NotNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                Typeface externalFont = Typeface.createFromAsset(getContext().getAssets(), "font/roboto_regular.ttf");
                ((TextView) v).setTypeface(externalFont);
                return v;
            }
        };
        addresModelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        propertyCreationBinding.areaType.setAdapter(addresModelArrayAdapter);
        propertyCreationBinding.areaType.setSelection(0);
    }

    private ArrayList<AreaModel> getAddressTypeListData() {
        ArrayList<AreaModel> addresModelArrayList = new ArrayList<>();
        AreaModel addresModel = new AreaModel();
        addresModel.setAreaType("Square Meters");
        addresModelArrayList.add(addresModel);
        addresModel = new AreaModel();
        addresModel.setAreaType("Square Feet");
        addresModelArrayList.add(addresModel);
        addresModel = new AreaModel();
        addresModel.setAreaType("Square yards");
        addresModelArrayList.add(addresModel);
        addresModel = new AreaModel();
        addresModel.setAreaType("Acres");
        addresModelArrayList.add(addresModel);
        return addresModelArrayList;
    }

    public void getStateList() {
        propertyCreationBinding.state.getEditText().setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "font/roboto_regular.ttf"));
        propertyCreationBinding.state.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "font/roboto_regular.ttf"));
        ArrayAdapter<StateModel> addresModelArrayAdapter = new ArrayAdapter<StateModel>(getApplicationContext(), android.R.layout.simple_spinner_item, getStateListData()) {
            @NotNull
            public View getView(int position, View convertView, @NotNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                Typeface externalFont = Typeface.createFromAsset(getContext().getAssets(), "font/roboto_regular.ttf");
                ((TextView) v).setTypeface(externalFont);
                return v;
            }

            public View getDropDownView(int position, View convertView, @NotNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                Typeface externalFont = Typeface.createFromAsset(getContext().getAssets(), "font/roboto_regular.ttf");
                ((TextView) v).setTypeface(externalFont);
                return v;
            }
        };
        addresModelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        propertyCreationBinding.state.setAdapter(addresModelArrayAdapter);
        propertyCreationBinding.state.setSelection(0);
    }

    private ArrayList<StateModel> getStateListData() {
        ArrayList<StateModel> stateModelArrayList = new ArrayList<>();
        StateModel stateModel = new StateModel();
        stateModel.setState("Telangana");
        stateModelArrayList.add(stateModel);
        stateModel = new StateModel();
        stateModel.setState("Andhrapradesh");
        stateModelArrayList.add(stateModel);
        stateModel = new StateModel();
        stateModel.setState("Karnataka");
        stateModelArrayList.add(stateModel);
        stateModel = new StateModel();
        stateModel.setState("Tamilanadu");
        stateModelArrayList.add(stateModel);
        return stateModelArrayList;
    }

    public void getPropertryList() {
        propertyCreationBinding.propertyType.getEditText().setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "font/roboto_regular.ttf"));
        propertyCreationBinding.propertyType.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "font/roboto_regular.ttf"));
        ArrayAdapter<PropertyTypeModel> addresModelArrayAdapter = new ArrayAdapter<PropertyTypeModel>(getApplicationContext(), android.R.layout.simple_spinner_item, getPropertyTypeListData()) {
            @NotNull
            public View getView(int position, View convertView, @NotNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                Typeface externalFont = Typeface.createFromAsset(getContext().getAssets(), "font/roboto_regular.ttf");
                ((TextView) v).setTypeface(externalFont);
                return v;
            }

            public View getDropDownView(int position, View convertView, @NotNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                Typeface externalFont = Typeface.createFromAsset(getContext().getAssets(), "font/roboto_regular.ttf");
                ((TextView) v).setTypeface(externalFont);
                return v;
            }
        };
        addresModelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        propertyCreationBinding.propertyType.setAdapter(addresModelArrayAdapter);
        propertyCreationBinding.propertyType.setSelection(0);
    }

    private ArrayList<PropertyTypeModel> getPropertyTypeListData() {
        ArrayList<PropertyTypeModel> propertyTypeModelArrayList = new ArrayList<>();
        PropertyTypeModel propertyTypeModel = new PropertyTypeModel();
        propertyTypeModel.setPropertyType("Real Estate");
        propertyTypeModelArrayList.add(propertyTypeModel);
        propertyTypeModel = new PropertyTypeModel();
        propertyTypeModel.setPropertyType("House");
        propertyTypeModelArrayList.add(propertyTypeModel);
        propertyTypeModel = new PropertyTypeModel();
        propertyTypeModel.setPropertyType("Agriculture Land");
        propertyTypeModelArrayList.add(propertyTypeModel);
        return propertyTypeModelArrayList;
    }

    @Override
    public void anotherizedToken() {

    }

    @Override
    public void onRemovePhoto(int position) {
        CutomAlertBox cutomAlertBox = new CutomAlertBox(PropertyCreation.this);

        cutomAlertBox.setTitle("Do you want to delete image ?");
        cutomAlertBox.setPositiveListener(view -> {
            mPaths.remove(position);
            photosUploadAdapter.notifyDataSetChanged();
            cutomAlertBox.dismiss();
            Toast.makeText(this, "Image removed successfully", Toast.LENGTH_SHORT).show();
        });
        cutomAlertBox.setNegativeListener(v -> cutomAlertBox.dismiss());
        cutomAlertBox.show();
    }

    @Override
    public void imageFullView(int position, String path) {
        propertyCreationBinding.fullView.setVisibility(View.VISIBLE);
        propertyCreationBinding.deleteFullView.setVisibility(View.VISIBLE);
        propertyCreationBinding.parent.setVisibility(View.GONE);
        Glide.with(this).load(path).into(propertyCreationBinding.fullView);
        propertyCreationBinding.deleteFullView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                propertyCreationBinding.fullView.setVisibility(View.GONE);
                propertyCreationBinding.deleteFullView.setVisibility(View.GONE);
                propertyCreationBinding.parent.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClickBack() {
        if (propertyCreationBinding.fullView.getVisibility() == View.VISIBLE) {
            propertyCreationBinding.fullView.setVisibility(View.GONE);
            propertyCreationBinding.deleteFullView.setVisibility(View.GONE);
            propertyCreationBinding.parent.setVisibility(View.VISIBLE);
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        onClickBack();
    }

    private class AreaModel {
        public String areaType;

        public String getAreaType() {
            return areaType;
        }

        public void setAreaType(String areaType) {
            this.areaType = areaType;
        }

        @Override
        public String toString() {
            return areaType;
        }
    }

    private class StateModel {
        public String state;

        public String getState() {
            return state;
        }

        public void setState(String areaType) {
            this.state = areaType;
        }

        @Override
        public String toString() {
            return state;
        }
    }

    private class PropertyTypeModel {
        public String propertyType;

        public String getPropertyType() {
            return propertyType;
        }

        public void setPropertyType(String propertyType) {
            this.propertyType = propertyType;
        }

        @Override
        public String toString() {
            return propertyType;
        }
    }

    private boolean validate() {
        String proName = propertyCreationBinding.propertyName.getText().toString().trim();
        String num = propertyCreationBinding.mobile.getText().toString().trim();
        String proType = propertyCreationBinding.propertyType.getSelectedItem().toString().trim();
        String proValue = propertyCreationBinding.propertyValue.getText().toString().trim();
        String village = propertyCreationBinding.village.getText().toString().trim();
        String mandal = propertyCreationBinding.mandal.getText().toString().trim();
        String state = propertyCreationBinding.state.getSelectedItem().toString().trim();
        String district = propertyCreationBinding.district.getText().toString().trim();

        if (isEmpty(proName)) {
            propertyCreationBinding.propertyName.setError("Please Enter Property Name!");
            propertyCreationBinding.propertyName.requestFocus();
            return false;
        } else if (proName.length() < 3) {
            propertyCreationBinding.propertyName.setError("Please enter above 3 characters!");
            propertyCreationBinding.propertyName.requestFocus();
            return false;
        } else if (isEmpty(num)) {
            propertyCreationBinding.mobile.setError("Please enter Mobile Number!");
            propertyCreationBinding.mobile.requestFocus();
            return false;
        } else if (num.length() < 10) {
            propertyCreationBinding.mobile.setError("Please enter 10 digiits Mobile Number!");
            propertyCreationBinding.mobile.requestFocus();
            return false;
        } else if (isEmpty(proType)) {
            propertyCreationBinding.propertyType.setError("Please Property Type!");
            propertyCreationBinding.propertyType.requestFocus();
            return false;
        } else if (isEmpty(proValue)) {
            propertyCreationBinding.propertyValue.setError("Please Enter Property Value!");
            propertyCreationBinding.propertyValue.requestFocus();
            return false;
        } else if (isEmpty(village)) {
            propertyCreationBinding.village.setError("Please Enter Village Name!");
            propertyCreationBinding.village.requestFocus();
            return false;
        } else if (village.length() < 3) {
            propertyCreationBinding.village.setError("Please enter above 3 characters!");
            propertyCreationBinding.village.requestFocus();
            return false;
        } else if (isEmpty(mandal)) {
            propertyCreationBinding.mandal.setError("Please Enter Mandal Name!");
            propertyCreationBinding.mandal.requestFocus();
            return false;
        } else if (mandal.length() < 3) {
            propertyCreationBinding.mandal.setError("Please enter above 3 characters!");
            propertyCreationBinding.mandal.requestFocus();
            return false;
        } else if (isEmpty(state)) {
            propertyCreationBinding.state.setError("Please Enter State Name!");
            propertyCreationBinding.state.requestFocus();
            return false;
        } else if (state.length() < 3) {
            propertyCreationBinding.state.setError("Please enter above 3 characters!");
            propertyCreationBinding.state.requestFocus();
            return false;
        } else if (isEmpty(district)) {
            propertyCreationBinding.district.setError("Please Enter District Name!");
            propertyCreationBinding.district.requestFocus();
            return false;
        } else if (district.length() < 3) {
            propertyCreationBinding.district.setError("Please enter above 3 characters!");
            propertyCreationBinding.district.requestFocus();
            return false;
        }

        return true;
    }
}
