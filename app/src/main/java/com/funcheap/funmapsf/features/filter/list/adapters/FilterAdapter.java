package com.funcheap.funmapsf.features.filter.list.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.funcheap.funmapsf.commons.models.Filter;
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager;

import java.util.List;

/**
 * Created by Jayson on 10/19/2017.
 *
 * Adapter for the RecyclerView in the ListFiltersActivity. Favors composition over inheritance.
 * {@link com.hannesdorfmann.adapterdelegates3.AdapterDelegate}s can be created for each
 * ViewType and attached to the {@link AdapterDelegatesManager} to handle heterogeneous views
 */

public class FilterAdapter extends RecyclerView.Adapter {

    private AdapterDelegatesManager<List<Filter>> delegatesManager;
    private List<Filter> mFilters;

    public FilterAdapter(Activity activity, List<Filter> filters) {
        this.mFilters = filters;

        delegatesManager = new AdapterDelegatesManager<>();

        // AdapterDelegatesManager internally assigns view types integers
        delegatesManager.addDelegate(new FilterDelegateAdapter(activity));

        // You can explicitly assign integer view type if you want to
        // delegatesManager.addDelegate(23, new SnakeAdapterDelegate(activity));
    }

    @Override
    public int getItemViewType(int position) {
        return delegatesManager.getItemViewType(mFilters, position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return delegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        delegatesManager.onBindViewHolder(mFilters, position, holder);
    }

    @Override
    public int getItemCount() {
        return mFilters.size();
    }

    /**
     * Replaces all {@link Filter} with filters passed in parameters and notifies the adapter
     * @param filters is a new list of Events
     */
    public void replaceEvents(List<Filter> filters) {
        mFilters = filters;
        this.notifyDataSetChanged();
    }
}
