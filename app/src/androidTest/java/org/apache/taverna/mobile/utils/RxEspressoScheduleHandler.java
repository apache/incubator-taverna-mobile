package org.apache.taverna.mobile.utils;


import android.support.annotation.NonNull;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.CountingIdlingResource;


import io.reactivex.functions.Function;
public class RxEspressoScheduleHandler implements Function<Runnable, Runnable> {

    private final CountingIdlingResource mCountingIdlingResource =
            new CountingIdlingResource("rxJava");

    @Override
    public Runnable apply(@NonNull final Runnable runnable) throws Exception {
        return new Runnable() {
            @Override
            public void run() {
                mCountingIdlingResource.increment();

                try {
                    runnable.run();
                } finally {
                    mCountingIdlingResource.decrement();
                }
            }
        };
    }

    public IdlingResource getIdlingResource() {
        return mCountingIdlingResource;
    }

}