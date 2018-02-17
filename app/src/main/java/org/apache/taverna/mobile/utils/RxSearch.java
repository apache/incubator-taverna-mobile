package org.apache.taverna.mobile.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;


public class RxSearch {

    public static Observable<String> fromSearchView(@NonNull final SearchView searchView) {
        final BehaviorSubject<String> subject = BehaviorSubject.create();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                subject.onNext(query);
                subject.onComplete();
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                subject.onNext(newText);
                return true;
            }
        });

        return subject;
    }
}