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

import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.commons.models.Filter;
import com.funcheap.funmapsf.commons.utils.ChipUtils;
import com.funcheap.funmapsf.features.filter.list.ListFilterViewModel;
import com.funcheap.funmapsf.features.filter.list.ListFiltersActivity;
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.vpaliy.chips_lover.ChipView;

import org.apmem.tools.layouts.FlowLayout;

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
    private ListFiltersActivity mActivity;

    public FilterDelegateAdapter(Activity activity) {
        this.mInflater = activity.getLayoutInflater();
        mListFilterViewModel = ViewModelProviders.of((FragmentActivity) activity).get(ListFilterViewModel.class);
        mActivity = (ListFiltersActivity) activity;
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

        viewHolder.txtTitle.setText(filter.getFilterName());

        // Populate Filter Chips Layout
        FlowLayout chipsLayout = viewHolder.chipsParams;
        chipsLayout.removeAllViews();
        List<ChipView> chipList = ChipUtils.chipsFromFilter(filter);

        for (ChipView chipView : chipList) {
            chipsLayout.addView(chipView);
            ((ViewGroup.MarginLayoutParams) chipView.getLayoutParams())
                    .setMargins(0, 0, 20, 20);
        }

        // Set the filter when it's clicked
        viewHolder.view.setOnClickListener(myView -> mActivity.onFilterClicked(filter));

        viewHolder.btnDelete.setOnClickListener(view -> {
            Log.d(TAG, "Deleted " + filter.getFilterName());
            mListFilterViewModel.deleteFilter(position);
        });

    }

    /**
     * ViewHolder to store references to the Layout Views for use when binding
     */
    public static class FilterViewHolder extends RecyclerView.ViewHolder {

        public View view;
        @BindView(R.id.text_title)
        public TextView txtTitle;
        @BindView(R.id.chip_params)
        public FlowLayout chipsParams;
        @BindView(R.id.img_delete_filter)
        public ImageView btnDelete;

        public FilterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.view = itemView;
        }
    }

}
