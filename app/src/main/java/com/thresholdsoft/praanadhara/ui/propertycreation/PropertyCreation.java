package com.thresholdsoft.praanadhara.ui.propertycreation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.databinding.ActivityPropertyCreationBinding;
import com.thresholdsoft.praanadhara.ui.base.BaseActivity;
import com.thresholdsoft.praanadhara.ui.propertycreation.adapter.PhotosUploadAdapter;
import com.thresholdsoft.praanadhara.ui.propertycreation.model.PropertyData;
import com.thresholdsoft.praanadhara.ui.propertysurveystatus.PropertyPreview;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static android.text.TextUtils.isEmpty;

public class PropertyCreation extends BaseActivity implements PropertyMvpView {
    @Inject
    PropertyMvpPresenter<PropertyMvpView> mpresenter;
    ActivityPropertyCreationBinding propertyCreationBinding;
    private int PICK_IMAGES = 1;
    private PhotosUploadAdapter photosUploadAdapter;

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
        getAddAddressTypes();
        getStateList();
        getPropertryList();
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
                    PropertyData propertyData = new PropertyData(propertyCreationBinding.propertyName.getText().toString(),
                            propertyCreationBinding.propertyType.getSelectedItem().toString(),
                            Double.parseDouble(propertyCreationBinding.propertyValue.getText().toString()),
                            propertyCreationBinding.village.getText().toString(),
                            propertyCreationBinding.mandal.getText().toString(),
                            propertyCreationBinding.state.getSelectedItem().toString(),
                            propertyCreationBinding.district.getText().toString(),
                            propertyCreationBinding.areaType.getSelectedItem().toString(), mPaths);
//                    List<PhotoUploadedData> photoUploadedDataList = new ArrayList<>();
//                    if (mPaths != null && mPaths.size() > 0) {
//                        for (String pathList : mPaths) {
//                            PhotoUploadedData photoUploadedData = new PhotoUploadedData(pathList);
//                            photoUploadedDataList.add(photoUploadedData);
//                        }
//                    }
                    mpresenter.insertPropertyData(propertyData);

                    Intent intent = new Intent(PropertyCreation.this, PropertyPreview.class);
                    intent.putExtra("propertyName", propertyCreationBinding.propertyName.getText().toString());
                    intent.putExtra("village", propertyCreationBinding.propertyName.getText().toString());
                    intent.putExtra("propertyId", mpresenter.propertyID());
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
            }
        });
    }

    private List<String> mPaths = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
        addresModel.setAreaType("Acres");
        addresModelArrayList.add(addresModel);
        addresModel = new AreaModel();
        addresModel.setAreaType("Square yards");
        addresModelArrayList.add(addresModel);
        addresModel = new AreaModel();
        addresModel.setAreaType("Square Meters");
        addresModelArrayList.add(addresModel);
        addresModel = new AreaModel();
        addresModel.setAreaType("SQ FT");
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
        mPaths.remove(position);
        photosUploadAdapter.notifyDataSetChanged();
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
