package com.thresholdsoft.wakfboard.ui.mapdataliastactivity;

import com.thresholdsoft.wakfboard.data.DataManager;
import com.thresholdsoft.wakfboard.ui.base.BasePresenter;
import com.thresholdsoft.wakfboard.ui.propertysurvey.model.MapDataTable;
import com.thresholdsoft.wakfboard.utils.rx.SchedulerProvider;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class MapDataListActivityPresenter<V extends MapDataListActivityMvpView> extends BasePresenter<V>
        implements MapDataListActivityMvpPresenter<V> {

    @Inject
    public MapDataListActivityPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }

    @Override
    public List<MapDataTable> getMapTypelist(int propertyId) {
        return getDataManager().getAllMapDtaListByPropertyId(propertyId);
    }

    @Override
    public void onClickSubmit() {
        getMvpView().onClickSubmit();
    }
}
