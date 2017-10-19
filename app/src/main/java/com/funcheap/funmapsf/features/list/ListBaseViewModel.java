package com.funcheap.funmapsf.features.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.funcheap.funmapsf.commons.models.Events;
import com.funcheap.funmapsf.commons.repository.EventsRepoSingleton;

import java.util.List;

/**
 * Created by Jayson on 10/13/2017.
 *
 * Base ViewModel for EventLists. This class holds references to our {@link LiveData} mEventsData
 * and our {@link EventsRepoSingleton} mEventsRepo for access through our subclasses.
 */

public class ListBaseViewModel extends ViewModel {

    protected EventsRepoSingleton mEventsRepo = EventsRepoSingleton.getEventsRepo();
    protected LiveData<List<Events>> mEventsData;

    public void setEventsData(LiveData<List<Events>> eventsData){
        mEventsData = eventsData;
    }

    public LiveData<List<Events>> getEventsData() {
        return  mEventsData;
    }

}
