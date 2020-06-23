package com.thresholdsoft.praanadhara.ui.mainactivity;

import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.data.db.model.FarmerLands;
import com.thresholdsoft.praanadhara.data.db.model.SurveyEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.MapTypeEntity;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveySaveReq;
import com.thresholdsoft.praanadhara.data.network.pojo.SurveyStartReq;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.ui.mainactivity.fragments.surveylistfrag.listener.ResultCallback;
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

public class MainActivityPresenter<V extends MainActivityMvpView> extends BasePresenter<V>
        implements MainActivityMvpPresenter<V> {

    @Inject
    public MainActivityPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }

    @Override
    public String getUserName() {
        return getDataManager().getUserName();
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

    @Override
    public void syncData() {
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
            getMvpView().syncComplete();
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
