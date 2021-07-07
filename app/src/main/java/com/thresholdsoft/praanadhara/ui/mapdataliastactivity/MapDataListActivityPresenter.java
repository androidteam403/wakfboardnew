package com.thresholdsoft.praanadhara.ui.mapdataliastactivity;

import com.thresholdsoft.praanadhara.data.DataManager;
import com.thresholdsoft.praanadhara.ui.base.BasePresenter;
import com.thresholdsoft.praanadhara.ui.propertysurvey.model.MapDataTable;
import com.thresholdsoft.praanadhara.utils.rx.SchedulerProvider;

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
}
