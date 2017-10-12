package com.funcheap.funmapsf.features.map;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.funcheap.funmapsf.commons.models.Events;
import com.funcheap.funmapsf.commons.repository.EventsRepoSingleton;

import java.util.List;

/**
 * Created by Jayson on 10/11/2017.
 *
 * Holds the state of the HomeActivity. {@link ViewModel}s survive rotation and
 * can be accessed by multiple activities/fragments, making it an ideal place to hold
 * view states.
 *
 * The ViewModel shouldn't know anything about the View using it.
 */

public class MapsViewModel extends ViewModel {
    private final String TAG = this.getClass().getSimpleName();

    EventsRepoSingleton mEventsRepo;

    // Holds the events to show on the map
    LiveData<List<Events>> mEventsLiveData;

    public MapsViewModel() {
        mEventsRepo = EventsRepoSingleton.getEventsRepo();
    }

    /**
     * Returns a {@link LiveData} object for a view to observe.
     * @return LiveData containing a list of events
     *
     * TODO This should factor any filter settings specified by the user
     */
    LiveData<List<Events>> getEventsLiveData() {
        if (mEventsLiveData == null) {
            mEventsLiveData = mEventsRepo.getEvents();
        }

        return mEventsLiveData;
    }

}
