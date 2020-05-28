package com.thresholdsoft.praanadhara.ui.main;

import com.thresholdsoft.praanadhara.data.network.pojo.FeedItem;
import com.thresholdsoft.praanadhara.ui.base.MvpView;

import java.util.List;

/**
 * Created on : Feb 11, 2019
 * Author     : AndroidWave
 */
public interface MainMvpView extends MvpView {
    void updateFeed(List<FeedItem> feedItemList);
}
