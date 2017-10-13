package com.funcheap.funmapsf.features.list.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.funcheap.funmapsf.commons.models.Events;
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager;

import java.util.List;

/**
 * Created by Jayson on 10/13/2017.
 *
 * Adapter for the RecyclerView in any ListFragment. Favors composition over inheritance.
 * {@link com.hannesdorfmann.adapterdelegates3.AdapterDelegate}s can be created for each
 * ViewType and attached to the {@link AdapterDelegatesManager} to handle heterogeneous views
 */

public class EventAdapter extends RecyclerView.Adapter {

    private AdapterDelegatesManager<List<Events>> delegatesManager;
    private List<Events> mEvents;

    public EventAdapter(Activity activity, List<Events> events) {
        this.mEvents = events;

        delegatesManager = new AdapterDelegatesManager<>();

        // AdapterDelegatesManager internally assigns view types integers
        delegatesManager.addDelegate(new EventDelegateAdapter(activity));

        // You can explicitly assign integer view type if you want to
        // delegatesManager.addDelegate(23, new SnakeAdapterDelegate(activity));
    }

    @Override
    public int getItemViewType(int position) {
        return delegatesManager.getItemViewType(mEvents, position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return delegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        delegatesManager.onBindViewHolder(mEvents, position, holder);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    /**
     * Replaces all {@link Events} with events passed in parameters and notifies the adapter
     * @param events is a new list of Events
     */
    public void replaceEvents(List<Events> events) {
        mEvents = events;
        this.notifyDataSetChanged();
    }
}
