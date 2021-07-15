package com.thresholdsoft.wakfboard.ui.gallery;

import com.thresholdsoft.wakfboard.data.DataManager;
import com.thresholdsoft.wakfboard.ui.base.BasePresenter;
import com.thresholdsoft.wakfboard.ui.propertysurvey.model.MapDataTable;
import com.thresholdsoft.wakfboard.utils.rx.SchedulerProvider;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class GalleryPresenter<V extends GalleryMvpView> extends BasePresenter<V>
        implements GalleryMvpPresenter<V> {
    @Inject
    public GalleryPresenter(DataManager manager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(manager, schedulerProvider, compositeDisposable);
    }

    @Override
    public List<MapDataTable> getAllMapDataTableListByPropertyid(int propertyId) {
        return getDataManager().getAllMapDtaListByPropertyId(propertyId);
    }

    @Override
    public void onClickBack() {
        getMvpView().onClickBack();
    }
}
