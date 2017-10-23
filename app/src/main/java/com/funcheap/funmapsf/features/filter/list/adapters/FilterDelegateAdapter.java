package com.funcheap.funmapsf.features.filter.list.adapters;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.commons.models.Filter;
import com.funcheap.funmapsf.features.filter.list.ListFilterViewModel;
import com.funcheap.funmapsf.features.home.HomeActivity;
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jayson on 10/19/2017.
 * <p>
 * Adapter Delegate that handles binding normal Filter items to a RecyclerView.
 */

public class FilterDelegateAdapter extends AdapterDelegate<List<Filter>> {

    private static final String FILTER_EXTRA = "filter_extra";
    private final String TAG = this.getClass().getSimpleName();
    private LayoutInflater mInflater;
    private ListFilterViewModel mListFilterViewModel;
    private HomeActivity mHomeActivity; // TODO Remove this once we move to ViewModels

    public FilterDelegateAdapter(Activity activity) {
        this.mInflater = activity.getLayoutInflater();
        mListFilterViewModel = ViewModelProviders.of((FragmentActivity) activity).get(ListFilterViewModel.class);
        mHomeActivity = (HomeActivity) activity;
    }

    @Override
    protected boolean isForViewType(@NonNull List<Filter> items, int position) {
        // Return true since we only have one Filter type for now
        return true;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.listitem_filter, parent, false);
        return new FilterViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(
            @NonNull List<Filter> items,
            int position,
            @NonNull RecyclerView.ViewHolder holder,
            @NonNull List<Object> payloads) {

        FilterViewHolder viewHolder = (FilterViewHolder) holder;
        Filter filter = items.get(position);

        String text = "Filter item " + items.get(position).id;
        String subtext = "Filter subtext " + items.get(position).id;
        viewHolder.txtTitle.setText(filter.getFilterName());
        viewHolder.txtParams.setText(filter.getQuery());

        viewHolder.view.setOnClickListener(myView -> {
            // TODO Handle filter through viewmodel and load MapFragment
            mHomeActivity.onFilterSaved(filter);
        });

        viewHolder.btnDelete.setOnClickListener(view -> {
            Log.d(TAG, "Delete button clicked!");
            Toast.makeText(view.getContext(), text + " deleted!", Toast.LENGTH_LONG).show();

            mListFilterViewModel.deleteFilter(position);
            filter.delete();
        });

    }

    /**
     * ViewHolder to store references to the Layout Views for use when binding
     */
    public static class FilterViewHolder extends RecyclerView.ViewHolder {

        public View view;
        @BindView(R.id.text_title)
        public TextView txtTitle;
        @BindView(R.id.text_params)
        public TextView txtParams;
        @BindView(R.id.img_delete_filter)
        public ImageView btnDelete;

        public FilterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.view = itemView;
        }
    }

}
