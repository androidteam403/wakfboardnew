package com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag;

import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.data.db.model.FarmerLands;
import com.thresholdsoft.praanadhara.data.db.model.LandEntity;
import com.thresholdsoft.praanadhara.data.db.model.SurveyEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.PicEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyDetailsEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyLandLocationEntity;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.FarmerLandReq;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.SurveyListModel;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.SurveyStatusCountModelRequest;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class SurveyListPresenter<V extends SurveyListMvpView> extends BasePresenter<V>
        implements SurveyListMvpPresenter<V> {
    PicEntity picEntity;
    private int pageNumber = 2;

    @Inject
    public SurveyListPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onItemClick(SurveyListModel farmerModel) {
        getMvpView().onItemClick(farmerModel);
    }

    @Override
    public void farmersListApiCall() {
        pageNumber = 2;
        getMvpView().showLoading();
        listApiCall("", 1, false);
    }

    @Override
    public void anotherizedTokenClearDate() {
        getDataManager().setUserLoggedOut();
    }

    @Override
    public void onClickNew() {
        pageNumber = 2;
        getMvpView().showLoading();
        listApiCall("New", 1, false);
    }

    @Override
    public void onClickInProgress() {
        pageNumber = 2;
        getMvpView().showLoading();
        listApiCall("InProgress", 1, false);
    }

    @Override
    public void onClickCompleted() {
        pageNumber = 2;
        getMvpView().showLoading();
        listApiCall("Completed", 1, false);
    }

    @Override
    public void pullToRefreshApiCall() {
        pageNumber = 2;
        listApiCall("", 1, false);
    }

    @Override
    public void loadMoreApiCall() {
        listApiCall("", pageNumber, true);
    }

    private void listApiCall(String status, int page, boolean isLoadMore) {
        FarmerLandReq farmerLandReq = new FarmerLandReq();
        farmerLandReq.setPage(page);
        farmerLandReq.setStatus(status);
        getCompositeDisposable().add(getDataManager()
                .doFarmerListApiCall(farmerLandReq)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(blogResponse -> {
                    if (blogResponse != null && blogResponse.getData() != null) {
                        if (blogResponse.getData().getListdata().getRows().size() > 0) {
                            if (isLoadMore) {
                                pageNumber++;
                                getMvpView().onSuccessLoadMore(blogResponse.getData().getListdata().getRows());
                            } else {
                                insertOrUpdateLands(blogResponse.getData().getListdata().getRows());
                            }
                        } else if (isLoadMore) {
                            getMvpView().onSuccessLoadMoreNodData();
                        } else {
                            getMvpView().displayNoData();
                        }
                    }
                    getMvpView().hideLoading();
                }, throwable -> {
                    getMvpView().hideLoading();
                    handleApiError(throwable);
                }));
    }

    @Override
    public void onStatusCountApiCall() {
        if (getMvpView().isNetworkConnected()) {
            getMvpView().showLoading();
            getMvpView().hideKeyboard();
            final SurveyStatusCountModelRequest request = new SurveyStatusCountModelRequest();
            getMvpView().showLoading();
            getCompositeDisposable().add(getDataManager()
                    .statusCount(request)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(blogResponse -> {
                        if (blogResponse != null && blogResponse.getData() != null && blogResponse.getSuccess()) {
                            getMvpView().onStatuCountApiSucess(blogResponse.getData());
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


    private void insertOrUpdateLands(List<RowsEntity> rows) {
        for (RowsEntity entity : rows) {
            FarmerLands farmerLands = new FarmerLands(entity.getUid(), entity.getName(), entity.getMobile(), entity.getEmail(), entity.getPic().size() > 0 ? entity.getPic().get(0).getPath() : "", entity.getFarmerLand().getUid(), entity.getFarmerLand().getPincode().getPincode(), entity.getFarmerLand().getPincode().getVillage().getName());
            getDataManager().insertFarmerLand(farmerLands);
            LandEntity landEntity = new LandEntity(entity.getFarmerLand().getUid(), entity.getFarmerLand().getSurveyLandLocation().getSubmitted().getUid(), entity.getFarmerLand().getSurveyLandLocation().getStartDate(),entity.getFarmerLand().getSurveyLandLocation().getSubmittedDate());
            getDataManager().insetLandEntity(landEntity);
            for(SurveyDetailsEntity surveyDetailsEntity : entity.getFarmerLand().getSurveyLandLocation().getSurveyDetails()) {
                SurveyEntity surveyEntity = new SurveyEntity(entity.getFarmerLand().getUid(), surveyDetailsEntity.getUid(), surveyDetailsEntity.getName(), surveyDetailsEntity.getDescription(), surveyDetailsEntity.getLatlongs(), surveyDetailsEntity.getMapType().getUid(), true);
                getDataManager().insetSurveyEntity(surveyEntity);
            }
        }
        loadListData();
    }

    private void loadListData() {
        List<SurveyListModel> surveyListModels = new ArrayList<>();
        List<FarmerLands> farmerLands = getDataManager().getAllFarmerLands();
        for(FarmerLands lands : farmerLands){
            LandEntity landEntity = getDataManager().getLandEntity(lands.getFarmerLandUid());
            surveyListModels.add(new SurveyListModel(lands.getUid(),lands.getName(),lands.getVillage(),lands.getMobile(),lands.getEmail(),lands.getPicPath(),
                    landEntity.getUid(),landEntity.getStatus(),landEntity.getStartDate(),landEntity.getSubmittedDate()));
        }
        getMvpView().onFarmersRes(surveyListModels);
    }
}