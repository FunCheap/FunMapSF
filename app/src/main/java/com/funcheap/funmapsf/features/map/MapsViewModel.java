package com.funcheap.funmapsf.features.map;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.funcheap.funmapsf.commons.models.Events;
import com.funcheap.funmapsf.commons.models.Filter;
import com.funcheap.funmapsf.commons.repository.EventsRepoSingleton;

import java.util.List;

/**
 * Created by Jayson on 10/11/2017.
 *
 * Holds the state of the HomeFragment. {@link ViewModel}s survive rotation and
 * can be accessed by multiple activities/fragments, making it an ideal place to hold
 * view states.
 *
 * The ViewModel shouldn't know anything about the View using it.
 */

public class MapsViewModel extends ViewModel {
    private final String TAG = this.getClass().getSimpleName();

    private EventsRepoSingleton mEventsRepo;

    // Whether or not to show a loading state
    private MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();
    // true -> ListMode, false -> MapMode
    private MutableLiveData<Boolean> mListMode;
    // true -> BookmarksMode, false -> SearchMode
    private MutableLiveData<Boolean> mBookmarksMode = new MutableLiveData<>();
    // The currently displayed filter
    private MutableLiveData<Filter> mCurrentFilter = new MutableLiveData<>();
    // The previous filter while the user is browsing bookmarks
    private Filter mTempFilter;
    // Holds the events to show on the map
    private LiveData<List<Events>> mEventsLiveData = Transformations.switchMap(mCurrentFilter,
            (filter) -> {
                mIsLoading.setValue(true);
                return mEventsRepo.getFilteredEvents(filter);
            });

    public MapsViewModel() {
        mEventsRepo = EventsRepoSingleton.getEventsRepo();
    }

    public LiveData<Boolean> isLoading() {
        return mIsLoading;
    }

    public void setLoading(boolean value){
        ((MutableLiveData)mIsLoading).setValue(value);
    }

    /**
     * Returns a {@link LiveData} object for a view to observe.
     * @return LiveData containing a list of events
     *
     * TODO This should factor any filter settings specified by the user
     */
    public LiveData<List<Events>> getEventsData() {
        // init filter if it doesn't exist

        mIsLoading.setValue(true);
        getFilter();
        return mEventsLiveData;
    }

    public LiveData<Filter> getFilter() {
        if (mCurrentFilter.getValue() == null) {
            mCurrentFilter.setValue(Filter.getDefaultFilter());
        }
        return mCurrentFilter;
    }

    public void setFilter(Filter filter) {
        mCurrentFilter.setValue(filter);
    }

    public void setEvents(List<Events> list){
        ((MutableLiveData)mEventsLiveData).setValue(list);
    }

    public LiveData<Boolean> getListMode() {
        if (mListMode == null) {
            mListMode = new MutableLiveData<>();
            mListMode.setValue(false);
        }
        return mListMode;
    }

    public void toggleListMode() {
        mListMode.setValue(!mListMode.getValue());
    }

}
