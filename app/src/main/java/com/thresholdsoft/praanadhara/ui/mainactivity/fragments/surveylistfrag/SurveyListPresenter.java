package com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag;

import androidx.lifecycle.LiveData;

import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.data.db.model.FarmerLands;
import com.thresholdsoft.praanadhara.data.db.model.SurveyEntity;
import com.thresholdsoft.praanadhara.data.db.model.SurveyStatusEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.MapTypeEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.RowsEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyDetailsEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveySaveReq;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.listener.ResultCallback;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.FarmerLandReq;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.SurveyStatusCountModelRequest;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.model.DeleteReq;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class SurveyListPresenter<V extends SurveyListMvpView> extends BasePresenter<V>
        implements SurveyListMvpPresenter<V> {
    private int pageNumber = 2;

    @Inject
    public SurveyListPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onItemClick(FarmerLands farmerModel) {
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
        if (getMvpView().isNetworkConnected()) {
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
    }

    @Override
    public void onStatusCountApiCall() {
        if (getMvpView().isNetworkConnected()) {
            getMvpView().showLoading();
            getMvpView().hideKeyboard();
            final SurveyStatusCountModelRequest request = new SurveyStatusCountModelRequest();
            getCompositeDisposable().add(getDataManager()
                    .statusCount(request)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(blogResponse -> {
                        if (blogResponse != null && blogResponse.getData() != null && blogResponse.getSuccess()) {
                            getDataManager().insertSurveyCount(new SurveyStatusEntity(blogResponse.getData().getInProgress(), blogResponse.getData().getCompleted(), blogResponse.getData().getNew(), blogResponse.getData().getTotal()));
                            getMvpView().onStatuCountApiSucess(blogResponse.getData());
                        }
                        getMvpView().hideLoading();
                    }, throwable -> {
                        getMvpView().hideLoading();
                        handleApiError(throwable);
                    }));

        }
    }

    @Override
    public LiveData<List<FarmerLands>> getAllFarmersLands() {
        return getDataManager().getAllFarmerLands();
    }

    @Override
    public LiveData<SurveyStatusEntity> getSurveyStatusCount() {
        return getDataManager().getSurveyCount();
    }


    private void insertOrUpdateLands(List<RowsEntity> rows) {
        for (RowsEntity entity : rows) {
            FarmerLands farmerLands = new FarmerLands(entity.getUid(), entity.getName(), entity.getMobile(), entity.getEmail(), entity.getPic().size() > 0 ? entity.getPic().get(0).getPath() : "", entity.getFarmerLand().getPincode().getPincode(), entity.getFarmerLand().getPincode().getVillage().getName(),
                    entity.getFarmerLand().getUid(), entity.getFarmerLand().getSurveyLandLocation().getUid(), entity.getFarmerLand().getSurveyLandLocation().getSubmitted().getUid() == null ? "New" : entity.getFarmerLand().getSurveyLandLocation().getSubmitted().getUid(), entity.getFarmerLand().getSurveyLandLocation().getStartDate(), entity.getFarmerLand().getSurveyLandLocation().getSubmittedDate());
            getDataManager().insertFarmerLand(farmerLands);
            for (SurveyDetailsEntity surveyDetailsEntity : entity.getFarmerLand().getSurveyLandLocation().getSurveyDetails()) {
                SurveyEntity surveyEntity = new SurveyEntity(entity.getFarmerLand().getUid(), surveyDetailsEntity.getUid(), entity.getFarmerLand().getSurveyLandLocation().getUid() == null ? "New" : entity.getFarmerLand().getSurveyLandLocation().getUid(), surveyDetailsEntity.getName(), surveyDetailsEntity.getDescription(), surveyDetailsEntity.getLatlongs(), surveyDetailsEntity.getMapType().getUid(), true);
                getDataManager().insetSurveyEntity(surveyEntity);
            }
        }
        main();
    }

    private static int cores = Runtime.getRuntime().availableProcessors();
    private static ExecutorService executorService = Executors.newFixedThreadPool(cores + 1);

    public void main() {
        System.out.println("Program Started");
        AtomicBoolean processing = new AtomicBoolean(true);

        syncLocalData(activities -> processing.set(false));

        while (processing.get()) {
            // keep running Wait for Sync Database
            processing.get();
        }
        stop();
        System.out.println("Program Terminated");
    }

    private void stop() {
        executorService.isShutdown();
    }

    private void syncLocalData(ResultCallback callback) {
        executorService.execute(() -> {
            Boolean surveyStartRes = false;
            Future<Boolean> editResponse = executorService.submit(updateEditSurvey());
            Future<Boolean> deleteResponse = executorService.submit(deleteSurvey());
            Future<Boolean> addResponse = executorService.submit(addSurvey());
            try {
                surveyStartRes = editResponse.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            try {
                surveyStartRes = deleteResponse.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            try {
                surveyStartRes = addResponse.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            callback.onResult(surveyStartRes);
        });

    }

    private Callable<Boolean> updateEditSurvey() {
        return () -> {
            List<SurveyEntity> editSurveysList = getDataManager().getAllSurveyEditList(true);
            AtomicInteger callCount = new AtomicInteger();
            for (SurveyEntity surveyEntity : editSurveysList) {
                callCount.getAndIncrement();
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
//                        .subscribeOn(getSchedulerProvider().io())
//                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(blogResponse -> {
                            callCount.getAndDecrement();
                            if (blogResponse != null && blogResponse.getData() != null && blogResponse.getSuccess()) {
                                getDataManager().updateSurveyEntity(surveyEntity);
                            }
                            getMvpView().hideLoading();
                        }, throwable -> {
                            getMvpView().hideLoading();
                            handleApiError(throwable);
                        }));
            }
            while (callCount.get() != 0) {
                // wait for Api response
                callCount.get();
            }
            return true;
        };
    }

    private Callable<Boolean> deleteSurvey() {
        return () -> {
            List<SurveyEntity> deleteSurveysList = getDataManager().getAllSurveyDeleteList(true);
            AtomicInteger callCount = new AtomicInteger();
            for (SurveyEntity surveyEntity : deleteSurveysList) {
                callCount.getAndIncrement();
                DeleteReq request = new DeleteReq();
                ArrayList<String> uids = new ArrayList<>();
                uids.add(surveyEntity.getUid());
                request.setUids(uids);
                getMvpView().showLoading();
                getCompositeDisposable().add(getDataManager()
                        .deleteSurvey(request)
//                        .subscribeOn(getSchedulerProvider().io())
//                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(blogResponse -> {
                            callCount.getAndDecrement();
                            if (blogResponse != null && blogResponse.getData() != null && blogResponse.getSuccess()) {
                                getDataManager().deleteSurveyEntity(surveyEntity);
                            }
                            getMvpView().hideLoading();
                        }, throwable -> {
                            getMvpView().hideLoading();
                            handleApiError(throwable);
                        }));
            }
            while (callCount.get() != 0) {
                // wait for Api response
                callCount.get();
            }
            return true;
        };
    }

    private Callable<Boolean> addSurvey() {
        return () -> {
            List<SurveyEntity> addSurveysList = getDataManager().getAllSurveySyncList(false);
            AtomicInteger callCount = new AtomicInteger();
            for (SurveyEntity surveyEntity : addSurveysList) {
                callCount.getAndIncrement();
                SurveySaveReq surveySaveReq = new SurveySaveReq();
                surveySaveReq.setSurvey(new SurveySaveReq.SurveyEntity(surveyEntity.getStartUid()));
                MapTypeEntity mapTypeEntity = new MapTypeEntity();
                mapTypeEntity.setUid(surveyEntity.getMapType());
                mapTypeEntity.setName(surveyEntity.getMapType());
                surveySaveReq.setMapType(mapTypeEntity);
                surveySaveReq.setLatlongs(surveyEntity.getLatLongs());
                surveySaveReq.setDescription(surveyEntity.getDescription());
                surveySaveReq.setName(surveyEntity.getName());
                getCompositeDisposable().add(getDataManager()
                        .saveSurvey(surveySaveReq)
//                        .subscribeOn(getSchedulerProvider().computation())
//                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(blogResponse -> {
                            callCount.getAndDecrement();
                            if (blogResponse != null && blogResponse.getData() != null && blogResponse.getSuccess()) {
                                surveyEntity.setUid(blogResponse.getData().getUid());
                                surveyEntity.setSync(true);
                                getDataManager().updateSurveyEntity(surveyEntity);
                            }
                            getMvpView().hideLoading();
                        }, throwable -> {
                            getMvpView().hideLoading();
                            handleApiError(throwable);
                        }));
            }
            while (callCount.get() != 0) {
                // wait for Api response
                callCount.get();
            }
            return true;
        };
    }
}