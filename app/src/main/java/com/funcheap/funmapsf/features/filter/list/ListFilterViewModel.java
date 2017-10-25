package com.funcheap.funmapsf.features.filter.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.funcheap.funmapsf.commons.models.Filter;
import com.funcheap.funmapsf.commons.repository.EventsRepoSingleton;

import java.util.List;

/**
 * Created by Jayson on 10/19/2017.
 *
 * ViewModel for the ListFiltersActivity
 */

public class ListFilterViewModel extends ViewModel {

    private final String TAG = this.getClass().getSimpleName();

    private EventsRepoSingleton mEventRepoSingleton = EventsRepoSingleton.getEventsRepo();

    private MutableLiveData<List<Filter>> mFiltersData;

    public LiveData<List<Filter>> getSavedFilters() {
        mFiltersData = (MutableLiveData<List<Filter>>) mEventRepoSingleton.getSavedFilters();
        return mFiltersData;
    }

    public void deleteFilter(int i) {
        List<Filter> filterList = mFiltersData.getValue();
        if (filterList.get(i) != null) {
            filterList.get(i).delete();
            filterList.remove(i);
        } else {
            Log.d(TAG, "deleteFilter: Tried to delete a non-existent filter!");
        }
        mFiltersData.setValue(filterList);
    }

    public void addFilter(Filter filter) {
        if (mFiltersData.getValue() != null) {
            mFiltersData.getValue().add(filter);
        }
    }
}
