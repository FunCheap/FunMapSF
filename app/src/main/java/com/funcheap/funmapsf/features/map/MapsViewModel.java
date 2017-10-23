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

    private MutableLiveData<Filter> mCurrentFilter = new MutableLiveData<>();
    // Holds the events to show on the map
    private LiveData<List<Events>> mEventsLiveData = Transformations.switchMap(mCurrentFilter,
            (filter) -> mEventsRepo.getFilteredEvents(filter));

    public MapsViewModel() {
        mEventsRepo = EventsRepoSingleton.getEventsRepo();
    }

    /**
     * Returns a {@link LiveData} object for a view to observe.
     * @return LiveData containing a list of events
     *
     * TODO This should factor any filter settings specified by the user
     */
    public LiveData<List<Events>> getEventsData() {
        // init filter if it doesn't exist
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

}
