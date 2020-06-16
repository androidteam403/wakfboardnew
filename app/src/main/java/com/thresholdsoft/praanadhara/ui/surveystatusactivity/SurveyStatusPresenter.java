package com.thresholdsoft.praanadhara.ui.surveystatusactivity;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;

import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.data.db.model.FarmerLands;
import com.thresholdsoft.praanadhara.data.db.model.SurveyEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.MapTypeEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveySaveReq;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyStartReq;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.model.DeleteReq;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class SurveyStatusPresenter<V extends SurveyStatusMvpView> extends BasePresenter<V>
        implements SurveyStatusMvpPresenter<V> {

    @Inject
    public SurveyStatusPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void startSurvey(FarmerLands rowsEntity) {
        if(getMvpView().isNetworkConnected()) {
            getMvpView().showLoading();
            getCompositeDisposable().add(getDataManager()
                    .startSurvey(new SurveyStartReq(new SurveyStartReq.LandLocationEntity(rowsEntity.getSurveyLandUid())))
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(blogResponse -> {
                        if (blogResponse != null && blogResponse.getData() != null && blogResponse.getSuccess()) {
                            getMvpView().startSurveySuccess();
                        }
                        getMvpView().hideLoading();
                    }, throwable -> {
                        getMvpView().hideLoading();
                        handleApiError(throwable);
                    }));
        }else{
            getMvpView().startSurveySuccess();
        }

    }


    @Override
    public void addSurvey(FarmerLands rowsEntity) {
        getMvpView().addSurvey(rowsEntity);
    }

    @Override
    public void submitSurvey(FarmerLands rowsEntity) {
        if(getMvpView().isNetworkConnected()) {
            SurveySaveReq.SurveyEntity landLocationEntity = new SurveySaveReq.SurveyEntity(rowsEntity.getUid());
            getMvpView().showLoading();
            getCompositeDisposable().add(getDataManager()
                    .submitSurvey(landLocationEntity)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(blogResponse -> {
                        if (blogResponse != null && blogResponse.getData() != null && blogResponse.getSuccess()) {
                            getMvpView().surveySubmitSuccess();
                        }
                        getMvpView().hideLoading();
                    }, throwable -> {
                        getMvpView().hideLoading();
                        handleApiError(throwable);
                    }));
        }else{
            getMvpView().surveySubmitSuccess();
        }
    }


    @Override
    public LiveData<FarmerLands> getFarmerLand(String uid, String landUid) {
        return getDataManager().getFarmerLand(uid, landUid);
    }

    @Override
    public void onpolygonRadioClick() {
        getMvpView().onpolygonRadioClick();
    }

    @Override
    public void onLinesRadioClick() {
        getMvpView().onLinesRadioClick();
    }

    @Override
    public void onPointsRadioClick() {
        getMvpView().onPointsRadioClick();
    }

    @Override
    public void deleteApiCall(SurveyEntity surveyEntity) {

        if (getMvpView().isNetworkConnected()) {
            getMvpView().showLoading();
            getMvpView().hideKeyboard();
            final DeleteReq request = new DeleteReq();
            ArrayList<String> uids = new ArrayList<>();
            uids.add(surveyEntity.getUid());
            request.setUids(uids);
            getMvpView().showLoading();
            getCompositeDisposable().add(getDataManager()
                    .deleteSurvey(request)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(blogResponse -> {
                        if (blogResponse != null && blogResponse.getData() != null && blogResponse.getSuccess()) {
                            getDataManager().deleteSurveyEntity(surveyEntity);
                        }
                        getMvpView().hideLoading();
                    }, throwable -> {
                        getMvpView().hideLoading();
                        handleApiError(throwable);
                    }));

        } else {
            if(TextUtils.isEmpty(surveyEntity.getUid())){
                getDataManager().deleteSurveyEntity(surveyEntity);
            }else {
                surveyEntity.setDelete(true);
                getDataManager().updateSurveyEntity(surveyEntity);
            }
        }
    }

    @Override
    public void editApiCal(SurveyEntity surveyEntity) {
        if (getMvpView().isNetworkConnected()) {
            getMvpView().showLoading();
            getMvpView().hideKeyboard();
            SurveySaveReq surveySaveReq = new SurveySaveReq();
            surveySaveReq.setUid(surveyEntity.getUid());
            surveySaveReq.setName(surveyEntity.getName());
            surveySaveReq.setDescription(surveyEntity.getDescription());
            surveySaveReq.setLatlongs(surveyEntity.getLatLongs());
            MapTypeEntity mapTypeEntity = new MapTypeEntity();
            mapTypeEntity.setUid(surveyEntity.getMapType());
            mapTypeEntity.setName(surveyEntity.getMapType());
            surveySaveReq.setMapType(mapTypeEntity);
            surveySaveReq.setSurvey(new SurveySaveReq.SurveyEntity(surveyEntity.getStartUid()));
            getCompositeDisposable().add(getDataManager()
                    .saveSurvey(surveySaveReq)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(blogResponse -> {
                        if (blogResponse != null && blogResponse.getData() != null && blogResponse.getSuccess()) {
                            getDataManager().updateSurveyEntity(surveyEntity);
                        }
                        getMvpView().hideLoading();
                    }, throwable -> {
                        getMvpView().hideLoading();
                        handleApiError(throwable);
                    }));

        } else {
            if(TextUtils.isEmpty(surveyEntity.getUid())){
                getDataManager().updateSurveyEntity(surveyEntity);
            }else {
                surveyEntity.setEdit(true);
                getDataManager().updateSurveyEntity(surveyEntity);
            }
        }
    }

    @Override
    public LiveData<List<SurveyEntity>> getAllSurveyList(String landUid) {
        return getDataManager().getAllSurveyList(landUid);
    }

    @Override
    public void updateFarmerLandStatus(String uid, String landUid) {
        FarmerLands lands = getDataManager().getFarmerLandDetails(uid, landUid);
        if (lands != null) {
            lands.setStatus("No");
            lands.setStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
            if(!getMvpView().isNetworkConnected()) {
                lands.setStart(true);
            }
            getDataManager().updateFarmerLand(lands);
        }
    }

    @Override
    public void updateLandSurveySubmit(String uid, String landUid){
        FarmerLands lands = getDataManager().getFarmerLandDetails(uid, landUid);
        if (lands != null) {
            lands.setStatus("Yes");
            lands.setSubmittedDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
            if(!getMvpView().isNetworkConnected()) {
                lands.setSubmit(true);
            }
            getDataManager().updateFarmerLand(lands);
        }
    }

}
