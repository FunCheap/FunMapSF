package com.funcheap.funmapsf.features.filter.list.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.commons.models.Filter;
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

    private LayoutInflater mInflater;
    private Filter mFilter;
    private Context mContext;
    private static final String FILTER_EXTRA = "filter_extra";

    public FilterDelegateAdapter(Activity activity) {
        this.mInflater = activity.getLayoutInflater();
        this.mContext = activity.getApplicationContext();
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
        view.setOnClickListener(myView -> {
            if (mFilter != null) {
                // TODO Handle filter item click here
            }
        });
        return new FilterViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(
            @NonNull List<Filter> items,
            int position,
            @NonNull RecyclerView.ViewHolder holder,
            @NonNull List<Object> payloads) {

        FilterViewHolder viewHolder = (FilterViewHolder) holder;
        mFilter = items.get(position);

        String text = "Filter item " + position;
        String subtext = "Filter subtext " + position;
        viewHolder.txtTitle.setText(text);
        viewHolder.txtParams.setText(subtext);
    }

    /**
     * ViewHolder to store references to the Layout Views for use when binding
     */
    public static class FilterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_title)
        public TextView txtTitle;
        @BindView(R.id.text_params)
        public TextView txtParams;
        @BindView(R.id.img_delete_filter)
        public ImageView btnDelete;

        public FilterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
