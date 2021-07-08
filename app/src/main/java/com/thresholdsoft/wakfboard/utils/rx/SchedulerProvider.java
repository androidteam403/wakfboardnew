package com.thresholdsoft.wakfboard.utils.rx;

import io.reactivex.Scheduler;

/**
 * Created by Jagadeesh on 30/03/2020.
 */


public interface SchedulerProvider {

    Scheduler ui();

    Scheduler computation();

    Scheduler io();

}
