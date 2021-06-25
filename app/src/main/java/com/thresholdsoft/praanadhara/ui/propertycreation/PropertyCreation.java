package com.thresholdsoft.praanadhara.ui.propertycreation;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.thresholdsoft.praanadhara.R;
import com.thresholdsoft.praanadhara.databinding.ActivityPropertyCreationBinding;
import com.thresholdsoft.praanadhara.ui.base.BaseActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import javax.inject.Inject;

public class PropertyCreation extends BaseActivity implements PropertyMvpView {
    @Inject
    PropertyMvpPresenter<PropertyMvpView> mpresenter;
    ActivityPropertyCreationBinding propertyCreationBinding;

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

    @Override
    public void anotherizedToken() {

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
}
