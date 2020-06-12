package com.thresholdsoft.praanadhara.ui.surveystatusactivity;

import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.data.db.model.FarmerLands;
import com.thresholdsoft.praanadhara.data.db.model.SurveyEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.MapTypeEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyDetailsEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveySaveReq;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyStartReq;
import com.thresholdsoft.praanadhara.ui.ApiClient;
import com.thresholdsoft.praanadhara.ui.ApiInterface;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.SurveyListModel;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.model.DeleteReq;
import com.thresholdsoft.praanadhara.ui.surveytrack.model.SurveyModel;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class SurveyStatusPresenter<V extends SurveyStatusMvpView> extends BasePresenter<V>
        implements SurveyStatusMvpPresenter<V> {

    @Inject
    public SurveyStatusPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void startSurvey(SurveyListModel rowsEntity) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .startSurvey(new SurveyStartReq(new SurveyStartReq.LandLocationEntity(rowsEntity.getLandUid())))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(blogResponse -> {
                    if (blogResponse != null && blogResponse.getData() != null && blogResponse.getSuccess()) {
                        getMvpView().startSurveySuccess(rowsEntity, blogResponse.getData());
                    }
                    getMvpView().hideLoading();
                }, throwable -> {
                    getMvpView().hideLoading();
                    handleApiError(throwable);
                }));


    }


    @Override
    public void addSurvey(SurveyListModel rowsEntity) {
        getMvpView().addSurvey(rowsEntity);
    }

    @Override
    public void submitSurvey(SurveyListModel rowsEntity) {
        SurveySaveReq.SurveyEntity landLocationEntity = new SurveySaveReq.SurveyEntity(rowsEntity.getUid());
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .submitSurvey(landLocationEntity)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(blogResponse -> {
                    if (blogResponse != null && blogResponse.getData() != null && blogResponse.getSuccess()) {
                        getMvpView().surveySubmitSuccess(blogResponse.getData());
                    }
                    getMvpView().hideLoading();
                }, throwable -> {
                    getMvpView().hideLoading();
                    handleApiError(throwable);
                }));
    }


    @Override
    public FarmerLands getFarmerLand(String uid, String landUid) {
       return getDataManager().getFarmerLand(uid,landUid);
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
    public void deleteApiCall(SurveyDetailsEntity farmerModel, int position) {

        if (getMvpView().isNetworkConnected()) {
            getMvpView().showLoading();
            getMvpView().hideKeyboard();
            ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
            final DeleteReq request = new DeleteReq();
            ArrayList<String> uids = new ArrayList<>();
            uids.add(farmerModel.getUid());
            request.setUids(uids);
            getMvpView().showLoading();
            getCompositeDisposable().add(getDataManager()
                    .deleteSurvey(request)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(blogResponse -> {
                        if (blogResponse != null && blogResponse.getData() != null && blogResponse.getSuccess()) {
                            getMvpView().onDeleteApiSuccess(position);
                        }
                        getMvpView().hideLoading();
                    }, throwable -> {
                        getMvpView().hideLoading();
                        handleApiError(throwable);
                    }));

        } else {
            getMvpView().showMessage("Please Connect to Proper internet");
        }
    }

    @Override
    public void editApiCal(SurveyDetailsEntity surveyDetailsEntity, int position) {
        if (getMvpView().isNetworkConnected()) {
            getMvpView().showLoading();
            getMvpView().hideKeyboard();
            SurveySaveReq surveySaveReq = new SurveySaveReq();
            surveySaveReq.setUid(surveyDetailsEntity.getUid());
            surveySaveReq.setDescription(surveyDetailsEntity.getDescription());
            surveySaveReq.setLatlongs(surveyDetailsEntity.getLatlongs());
            surveySaveReq.setMapType(surveyDetailsEntity.getMapType());
            surveySaveReq.setSurvey(new SurveySaveReq.SurveyEntity(getMvpView().getSurvey().getUid()));
            getCompositeDisposable().add(getDataManager()
                    .saveSurvey(surveySaveReq)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(blogResponse -> {
                        if (blogResponse != null && blogResponse.getData() != null && blogResponse.getSuccess()) {
                            getMvpView().onSuccessEditSurvey(surveyDetailsEntity.getDescription(), position);
                        }
                        getMvpView().hideLoading();
                    }, throwable -> {
                        getMvpView().hideLoading();
                        handleApiError(throwable);
                    }));

        } else {
            getMvpView().showMessage("Please Connect to Proper internet");
        }
    }

    @Override
    public ArrayList<SurveyDetailsEntity> getAllSurveyList(String landUid) {
        ArrayList<SurveyDetailsEntity> surveyModelList = new ArrayList<>();
        List<SurveyEntity> surveyEntities = getDataManager().getAllSurveyList(landUid);
        if(surveyEntities != null) {
            for (SurveyEntity surveyEntity : surveyEntities) {
                MapTypeEntity mapTypeEntity = new MapTypeEntity();
                mapTypeEntity.setName(surveyEntity.getMapType());
                mapTypeEntity.setUid(surveyEntity.getMapType());
                surveyModelList.add(new SurveyDetailsEntity(surveyEntity.getName(), surveyEntity.getDescription(), surveyEntity.getLatLongs(), mapTypeEntity, surveyEntity.getUid()));
            }
        }
        return surveyModelList;
    }

    @Override
    public void updateFarmerLandStatus(String uid, String landUid) {
        FarmerLands lands = getDataManager().getFarmerLand(uid,landUid);
        if(lands != null){
            lands.setStatus("No");
            getDataManager().updateFarmerLand(lands);
        }
    }


}
