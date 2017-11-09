package com.funcheap.funmapsf.features.list.cluster;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funcheap.funmapsf.commons.models.Events;
import com.funcheap.funmapsf.features.list.ListBaseFragment;

import java.util.Collections;
import java.util.List;

/**
 * Created by Jayson on 10/11/2017.
 * <p>
 * Fragment for displaying a list of events from a clicked event cluster. It should
 * display a list of events that correspond to a clicked "Event Cluster" from the MapFragment
 */

public class ListClusterFragment extends ListBaseFragment {

    public static final String EVENT_LIST_EXTRA = "event_list_extra";

    public static Fragment newInstance() {
        Bundle args = new Bundle();
        // Set any input args here
        ListClusterFragment fragment = new ListClusterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected LiveData<List<Events>> getEventData() {
        // Get passed cluster and hand it to our ViewModel
        Intent intent = getActivity().getIntent();
        List<Events> eventsList = intent.getParcelableArrayListExtra(EVENT_LIST_EXTRA);

        Collections.sort(eventsList,
                (event1, event2) -> event1.getStartDate().compareTo(event2.getStartDate()));

        MutableLiveData<List<Events>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(eventsList);
        return mutableLiveData;
    }
}
