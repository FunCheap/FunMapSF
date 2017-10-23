package com.funcheap.funmapsf.features.list.bookmarks;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.funcheap.funmapsf.commons.models.Events;
import com.funcheap.funmapsf.features.list.ListBaseViewModel;

import java.util.List;

/**
 * Created by Jayson on 10/13/2017.
 *
 * ViewModel to hold the state of the ListBookmarksFragment
 */

public class ListBookmarksViewModel extends ListBaseViewModel {

    public LiveData<List<Events>> getBookmarksEventsData() {
        if (mEventsData == null) {
            // Call our model to get the appropriate LiveData
            mEventsData = (MutableLiveData<List<Events>>) mEventsRepo.getBookmarkedEvents();
        }
        return mEventsData;
    }
}
