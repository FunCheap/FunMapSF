package com.funcheap.funmapsf.features.list;

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

    @BindView(R.id.recycler_event_list)
    protected RecyclerView mEventRecycler;

    /**
     * Subclasses must implement this method to load and bind a datasource to the recycler
     * view. Request a LiveData<List<Event>> from the appropriate ViewModel by calling
     * <pre><code>
     *     MyViewModel.getLiveData().observe( data -> { onBindData(data) } );
     * </code></pre>
     */
    protected abstract void bindData();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_event_list, container, false);
        ButterKnife.bind(this, root);

        initRecyclerView();
        bindData();

        return root;
    }

    private void initRecyclerView() {
        mEventRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mEventRecycler.setAdapter(
                new EventAdapter(getActivity(), new ArrayList<>()));
    }

    protected void onBindData(List<Events> eventsList) {
        ((EventAdapter) mEventRecycler.getAdapter()).replaceEvents(eventsList);
    }
}
