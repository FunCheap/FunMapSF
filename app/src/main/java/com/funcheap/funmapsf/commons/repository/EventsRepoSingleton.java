package com.funcheap.funmapsf.commons.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.VisibleForTesting;

import com.funcheap.funmapsf.commons.models.Events;
import com.funcheap.funmapsf.commons.models.Venue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jayson on 10/11/2017.
 * <p>
 * This class handles requests for events from different ViewModels. Incoming requests
 * should be serviced from the Firebase database or our local database depending on our network
 * status and the freshness of our local data.
 */

public class EventsRepoSingleton {

    private static EventsRepoSingleton mEventsRepo;

    private EventsRepoSingleton(){}

    /*
     * Public accessor
     */
    public static EventsRepoSingleton getEventsRepo() {
        if (mEventsRepo == null) {
            mEventsRepo = new EventsRepoSingleton();
        }
        return mEventsRepo;
    }

    /**
     * Example event request
     * @param filters is an object holding our filter settings (date range, price, etc...
     *
     * TODO This should factor any filter settings specified by the user
     */
    public LiveData<List<Events>> getEvents() {
        MutableLiveData<List<Events>> eventsLiveData = new MutableLiveData<>();

        /* Do some async work to populate the events list
        onAsyncCompletedCallback(List<Events> populatedEvents) {
            eventsLiveData.setValue(populatedEvents)
            // At this point any View observing the originally returned LiveData will be notified
            // and can update based on the populatedEvents
        }
         */

        // Returns an empty list at first. The list is replaced when the async work finishes
        // and all observing views are notified.
        // Return dummy data for now
        eventsLiveData.setValue(generateDummyEvents());
        return eventsLiveData;
    }

    /**
     * Generates dummy {@link Events} for testing
     * @return A List of dummy events
     */
    List<Events> generateDummyEvents() {
        List<Events> eventsList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Events dummyEvent = new Events();
            dummyEvent.setTitle("Event " + i);
            dummyEvent.setContent("Content " + i);
            dummyEvent.setCost("$" + i);
            dummyEvent.setStartDate("October 13, 2017");
            dummyEvent.setEndDate("October 14, 2017");

            Venue dummyVenue = new Venue();
            dummyVenue.setName("Somewhere in SF");
            dummyEvent.setVenue(dummyVenue);

            // TODO Populate more dummy fields

            eventsList.add(dummyEvent);
        }

        return eventsList;
    }

}
