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
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyStartReq;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.listener.ResultCallback;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.FarmerLandReq;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.model.SurveyStatusCountModelRequest;
import com.thresholdsoft.praanadhara.ui.surveystatusactivity.model.DeleteReq;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.Collections;
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

    @Inject
    public SurveyListPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onItemClick(FarmerLands farmerModel) {
        getMvpView().onItemClick(farmerModel);
    }


    @Override
    public void anotherizedTokenClearDate() {
        getDataManager().setUserLoggedOut();
    }

    @Override
    public void onClickNew() {
        getMvpView().statusBaseListFilter("New");
        getMvpView().onClickNew();
    }

    @Override
    public void onClickInProgress() {
        getMvpView().statusBaseListFilter("InProgress");
        getMvpView().onClickInProgress();
    }

    @Override
    public void onClickCompleted() {
        getMvpView().statusBaseListFilter("Completed");
        getMvpView().onClickCompleted();
    }

    @Override
    public void pullToRefreshApiCall() {
        onStatusCountApiCall(true);
    }

    @Override
    public void loadMoreApiCall(int i) {
        listApiCall( 10,i, true, false);
    }

    private void listApiCall(int rowsCount, int page, boolean isLoadMore, boolean isPullToRefresh) {
        if (getMvpView().isNetworkConnected()) {
            FarmerLandReq farmerLandReq = new FarmerLandReq();
            farmerLandReq.setPage(page);
            farmerLandReq.setRows(rowsCount);
            farmerLandReq.setStatus("");
            getCompositeDisposable().add(getDataManager()
                    .doFarmerListApiCall(farmerLandReq)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(blogResponse -> {
                        if (blogResponse != null && blogResponse.getData() != null) {
                            if (blogResponse.getData().getListdata().getRows().size() > 0) {
                                if (isLoadMore) {
                                    getMvpView().onSuccessLoadMore();
                                } else if (isPullToRefresh) {
                                    getMvpView().onSuccessPullToRefresh();
                                }
                                insertOrUpdateLands(blogResponse.getData().getListdata().getPage(), blogResponse.getData().getListdata().getRows());
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
    public void onStatusCountApiCall(boolean isPullToRefresh) {
        if (getMvpView().isNetworkConnected()) {
            if(!isPullToRefresh) {
                getMvpView().showLoading();
            }
            getMvpView().hideKeyboard();
            final SurveyStatusCountModelRequest request = new SurveyStatusCountModelRequest();
            getCompositeDisposable().add(getDataManager()
                    .statusCount(request)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(blogResponse -> {
                        if (blogResponse != null && blogResponse.getData() != null && blogResponse.getSuccess()) {
                            getDataManager().insertSurveyCount(new SurveyStatusEntity(blogResponse.getData().getInProgress(), blogResponse.getData().getCompleted(), blogResponse.getData().getNew(), blogResponse.getData().getTotal()));
                            listApiCall(blogResponse.getData().getTotal(),1,false,isPullToRefresh);
                        }
                        getMvpView().hideLoading();
                    }, throwable -> {
                        getMvpView().hideLoading();
                        handleApiError(throwable);
                    }));

        }else if (isPullToRefresh) {
            getMvpView().onSuccessPullToRefresh();
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


    private void insertOrUpdateLands( int page, List<RowsEntity> rows) {
        int position = 0;
         Collections.reverse(rows);
        for (RowsEntity entity : rows) {
            position++;
            FarmerLands farmerLands = new FarmerLands(page, position, entity.getUid(), entity.getName(), entity.getMobile(), entity.getEmail(), entity.getPic().size() > 0 ? entity.getPic().get(0).getPath() : "", entity.getFarmerLand().getPincode().getPincode(), entity.getFarmerLand().getPincode().getVillage().getName(),
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

    private boolean offlineSurveyStartSync(){
        Boolean surveyStartRes = false;
        Future<Boolean> startResponse = executorService.submit(startSurvey());
        try {
            surveyStartRes = startResponse.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return surveyStartRes;
    }

    private boolean offlineSurveySubmitSync(){
        Boolean surveyStartRes = false;
        Future<Boolean> submitResponse = executorService.submit(submitSurvey());
        try {
            surveyStartRes = submitResponse.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return surveyStartRes;
    }

    public void main() {
        System.out.println("Program Started");
        if(offlineSurveyStartSync()) {
            AtomicBoolean processing = new AtomicBoolean(true);

            syncLocalData(activities -> processing.set(false));

            while (processing.get()) {
                // keep running Wait for Sync Database
                processing.get();
            }
        }

        if(offlineSurveySubmitSync()){
            stop();
            System.out.println("Program Terminated");
        }
    }

    private void stop() {
        executorService.isShutdown();
    }

    private void syncLocalData(ResultCallback callback) {
        executorService.execute(() -> {
            Boolean surveyStartRes = false;
      //      Future<Boolean> startResponse = executorService.submit(startSurvey());
            Future<Boolean> addResponse = executorService.submit(addSurvey());
            Future<Boolean> editResponse = executorService.submit(updateEditSurvey());
            Future<Boolean> deleteResponse = executorService.submit(deleteSurvey());
       //     Future<Boolean> submitResponse = executorService.submit(submitSurvey());

//            try {
//                surveyStartRes = startResponse.get();
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
            try {
                surveyStartRes = addResponse.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
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
//            try {
//                surveyStartRes = submitResponse.get();
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }


            callback.onResult(surveyStartRes);
        });

    }

    private Callable<Boolean> startSurvey() {
        return () -> {
            List<FarmerLands> startSurveysList = getDataManager().getAllSurveyStartList(true);
            AtomicInteger callCount = new AtomicInteger();
            for (FarmerLands surveyEntity : startSurveysList) {
                callCount.getAndIncrement();
                getCompositeDisposable().add(getDataManager()
                        .startSurvey(new SurveyStartReq(new SurveyStartReq.LandLocationEntity(surveyEntity.getFarmerLandUid())))
//                        .subscribeOn(getSchedulerProvider().io())
//                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(blogResponse -> {
                            callCount.getAndDecrement();
                            if (blogResponse != null && blogResponse.getData() != null && blogResponse.getSuccess()) {
                                surveyEntity.setStart(false);
                                surveyEntity.setSurveyLandUid(blogResponse.getData().getUid());
                                getDataManager().updateFarmerLand(surveyEntity);
//                                SurveyEntity entity = getDataManager().getSurveyLandEntity(surveyEntity.getFarmerLandUid());
//                                entity.setStartUid(blogResponse.getData().getUid());
//                                getDataManager().updateSurveyEntity(entity);
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
                                surveyEntity.setEdit(false);
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
                                surveyEntity.setDelete(false);
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
                FarmerLands lands = getDataManager().getFarmerLandDetails(surveyEntity.getLandUid());
                SurveySaveReq surveySaveReq = new SurveySaveReq();
                surveySaveReq.setSurvey(new SurveySaveReq.SurveyEntity(lands.getSurveyLandUid()));
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

    private Callable<Boolean> submitSurvey() {
        return () -> {
            List<FarmerLands> addSurveysList = getDataManager().getAllSurveySubmitList(true);
            AtomicInteger callCount = new AtomicInteger();
            for (FarmerLands surveyEntity : addSurveysList) {
                callCount.getAndIncrement();
                SurveySaveReq.SurveyEntity landLocationEntity = new SurveySaveReq.SurveyEntity(surveyEntity.getSurveyLandUid());
                getCompositeDisposable().add(getDataManager()
                        .submitSurvey(landLocationEntity)
//                        .subscribeOn(getSchedulerProvider().io())
//                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(blogResponse -> {
                            callCount.getAndDecrement();
                            if (blogResponse != null && blogResponse.getData() != null && blogResponse.getSuccess()) {
                                surveyEntity.setSubmit(false);
                                getDataManager().updateFarmerLand(surveyEntity);
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