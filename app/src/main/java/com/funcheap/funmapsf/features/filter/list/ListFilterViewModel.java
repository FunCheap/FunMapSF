package com.funcheap.funmapsf.features.filter.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.funcheap.funmapsf.commons.models.Filter;
import com.funcheap.funmapsf.commons.repository.EventsRepoSingleton;

import java.util.List;

/**
 * Created by Jayson on 10/19/2017.
 *
 * ViewModel for the ListFiltersActivity
 */

public class ListFilterViewModel extends ViewModel {

    EventsRepoSingleton mEventRepoSingleton = EventsRepoSingleton.getEventsRepo();

    private MutableLiveData<List<Filter>> mFiltersData;

    public LiveData<List<Filter>> getSavedFilters() {
        return mEventRepoSingleton.getSavedFilters();
    }

}
