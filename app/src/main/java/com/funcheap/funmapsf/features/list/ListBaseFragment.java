package com.funcheap.funmapsf.features.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.commons.models.Events;
import com.funcheap.funmapsf.features.list.adapters.EventAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jayson on 10/11/2017.
 *
 * The base fragment used to display events in a List.
 */

public abstract class ListBaseFragment extends Fragment {

    private ListBaseViewModel mListBaseViewModel;

    @BindView(R.id.recycler_event_list)
    protected RecyclerView mEventRecycler;

    /**
     * Subclasses must implement this method to provide datasource to the recycler
     * view. Request a LiveData<List<Event>> from the appropriate ViewModel by calling
     * <pre><code>
     *     return MyViewModel.getSomeLiveData();
     * </code></pre>
     *
     * You can also create your own LiveData like so
     * <pre><code>
     *     MutableLiveData<List<Events>> eventsData = new MutableLiveData<<>>();
     *     eventsData.setValue(someEventsList);
     *     return eventsData;
     * </code></pre>
     */
    protected abstract LiveData<List<Events>> getEventData();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_event_list, container, false);
        ButterKnife.bind(this, root);

        mListBaseViewModel = ViewModelProviders.of(getActivity()).get(ListBaseViewModel.class);

        initRecyclerView();
        mListBaseViewModel.setEventsData(getEventData());
        bindData();

        return root;
    }

    private void initRecyclerView() {
        mEventRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mEventRecycler.setAdapter(
                new EventAdapter(getActivity(), new ArrayList<>()));
    }

    protected void bindData() {
        mListBaseViewModel.getEventsData().observe(this, events -> {
            this.onEventsChanged(events);
            mEventRecycler.scrollToPosition(0);
        });
    }

    /**
     * This runs each time a change is detected in the list of Events.
     * @param eventsList the new list of Events.
     */
    protected void onEventsChanged(List<Events> eventsList) {
        ((EventAdapter) mEventRecycler.getAdapter()).replaceEvents(eventsList);
    }
}
