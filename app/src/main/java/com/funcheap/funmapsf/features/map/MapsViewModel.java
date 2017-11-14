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
 * <p>
 * Holds the state of the HomeFragment. {@link ViewModel}s survive rotation and
 * can be accessed by multiple activities/fragments, making it an ideal place to hold
 * view states.
 * <p>
 * The ViewModel shouldn't know anything about the View using it.
 */

public class MapsViewModel extends ViewModel {
    private final String TAG = this.getClass().getSimpleName();

    public static final int SEARCH_MODE = 0;
    public static final int BOOKMARKS_MODE = 1;

    private EventsRepoSingleton mEventsRepo;

    // Whether or not to show a loading state
    private MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();
    // true -> ListMode, false -> MapMode
    private MutableLiveData<Boolean> mListMode = new MutableLiveData<>();
    private MutableLiveData<Integer> mDisplayMode = new MutableLiveData<>();
    // The currently displayed filter
    private MutableLiveData<Filter> mCurrentFilter = new MutableLiveData<>();
    // The previous filter while the user is browsing bookmarks
    private Filter mTempFilter;
    private List<Events> mTempEvents;
    // Holds the events to show on the map
    private LiveData<List<Events>> mEventsLiveData = Transformations.switchMap(mCurrentFilter,
            (filter) -> {
                mIsLoading.setValue(true);
                if (mDisplayMode.getValue() == BOOKMARKS_MODE) {
                    // Load bookmarks
                    return mEventsRepo.getBookmarkedEvents();
                } else {
                    // Load search
                    if (filter == mTempFilter) {
                        // Load from cache
                        MutableLiveData<List<Events>> events = new MutableLiveData<>();
                        events.setValue(mTempEvents);
                        return events;
                    } else {
                        // Load new search
                        return mEventsRepo.getFilteredEvents(filter);
                    }
                }
            });

    public MapsViewModel() {
        mEventsRepo = EventsRepoSingleton.getEventsRepo();

        mDisplayMode.setValue(SEARCH_MODE);
        mListMode.setValue(true);
    }

    public LiveData<Boolean> isLoading() {
        return mIsLoading;
    }

    public void setLoading(boolean value) {
        mIsLoading.setValue(value);
    }

    /**
     * Returns a {@link LiveData} object for a view to observe.
     *
     * @return LiveData containing a list of events
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

    public void setEvents(List<Events> list) {
        ((MutableLiveData) mEventsLiveData).setValue(list);
    }

    public LiveData<Boolean> getListMode() {
        return mListMode;
    }

    public void toggleListMode() {
        mListMode.setValue(!mListMode.getValue());
    }

    public LiveData<Integer> getDisplayMode() {
        return mDisplayMode;
    }

    public void setDisplayMode(int displayMode) {
        if (mDisplayMode.getValue() == SEARCH_MODE) {
            // If in search mode, Save the current filter
            mTempFilter = mCurrentFilter.getValue();
            mTempEvents = mEventsLiveData.getValue();
        }

        if (displayMode == BOOKMARKS_MODE) {
            // If we were already in bookmarks mode
            // Refresh displayed bookmarks
            mDisplayMode.setValue(displayMode);
            setFilter(null);
        } else if (mDisplayMode.getValue() == BOOKMARKS_MODE && displayMode == SEARCH_MODE) {
            // Moving from bookmarks to search
            // Restore filter
            mDisplayMode.setValue(displayMode);
            setFilter(mTempFilter);
        }
    }

}
