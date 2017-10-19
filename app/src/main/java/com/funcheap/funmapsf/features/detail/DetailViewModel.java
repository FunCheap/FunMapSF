package com.funcheap.funmapsf.features.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.funcheap.funmapsf.commons.models.Events;

/**
 * Created by Jayson on 10/11/2017.
 *
 * ViewModel to hold the state of the DetailActivity
 */

public class DetailViewModel extends ViewModel {
    private MutableLiveData<Events> eventData;

    public void setEventData(Events event) {
        if (eventData == null) {
            eventData = new MutableLiveData<>();
        }
        eventData.setValue(event);
    }

    public LiveData<Events> getEventData() {
        return eventData;
    }

}
