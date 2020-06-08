package com.thresholdsoft.praanadhara.ui.surveystatusactivity;

import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyDetailsEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveySaveReq;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyStartReq;
import com.thresholdsoft.praanadhara.ui.ApiClient;
import com.thresholdsoft.praanadhara.ui.ApiInterface;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.model.DeleteReq;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class SurveyStatusPresenter<V extends SurveyStatusMvpView> extends BasePresenter<V>
        implements SurveyStatusMvpPresenter<V> {

    @Inject
    public SurveyStatusPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void startSurvey(RowsEntity rowsEntity) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .startSurvey(new SurveyStartReq(new SurveyStartReq.LandLocationEntity(rowsEntity.getFarmerLand().getUid())))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(blogResponse -> {
                    if (blogResponse != null && blogResponse.getData() != null && blogResponse.getSuccess()) {
                        rowsEntity.setStartSurveyUid(blogResponse.getData().getUid());
                        getMvpView().startSurveySuccess(rowsEntity, blogResponse.getData());
                    }
                    getMvpView().hideLoading();
                }, throwable -> {
                    getMvpView().hideLoading();
                    handleApiError(throwable);
                }));


    }


    @Override
    public void addSurvey(RowsEntity rowsEntity) {
        getMvpView().addSurvey(rowsEntity);
    }

    @Override
    public void submitSurvey(RowsEntity rowsEntity) {
        SurveySaveReq.SurveyEntity landLocationEntity = new SurveySaveReq.SurveyEntity(rowsEntity.getStartSurveyUid());
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


}
