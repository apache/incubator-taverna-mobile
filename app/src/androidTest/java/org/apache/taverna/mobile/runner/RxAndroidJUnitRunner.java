package org.apache.taverna.mobile.runner;

import org.apache.taverna.mobile.utils.RxEspressoScheduleHandler;

import android.os.Bundle;
import android.support.test.espresso.Espresso;

import io.reactivex.plugins.RxJavaPlugins;

public class RxAndroidJUnitRunner extends UnlockDeviceAndroidJUnitRunner {

    @Override
    public void onCreate(Bundle arguments) {
        super.onCreate(arguments);

        RxEspressoScheduleHandler rxEspressoScheduleHandler = new RxEspressoScheduleHandler();
        RxJavaPlugins.setScheduleHandler(rxEspressoScheduleHandler);
        Espresso.registerIdlingResources(rxEspressoScheduleHandler.getIdlingResource());
    }

}