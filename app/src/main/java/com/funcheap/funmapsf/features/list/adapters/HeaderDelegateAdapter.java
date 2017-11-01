package com.funcheap.funmapsf.features.list.adapters;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.commons.models.Events;
import com.funcheap.funmapsf.commons.utils.DateCostFormatter;
import com.funcheap.funmapsf.features.detail.DetailActivity;
import com.funcheap.funmapsf.features.list.ListBaseViewModel;
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.funcheap.funmapsf.R.id.ivItemImg;

/**
 * Created by Jayson on 10/13/2017.
 * <p>
 * Adapter Delegate that handles binding normal Event items to a RecyclerView.
 */

public class HeaderDelegateAdapter extends AdapterDelegate<List<Events>> {

    private LayoutInflater mInflater;
    private Context mContext;
    private ListBaseViewModel mListBaseViewModel;
    private final String TAG = this.getClass().getSimpleName();
    private static final String EVENT_EXTRA = "event_extra";

    public HeaderDelegateAdapter(Activity activity) {
        this.mInflater = activity.getLayoutInflater();
        this.mContext = activity;
        mListBaseViewModel = ViewModelProviders.of((FragmentActivity) activity).get(ListBaseViewModel.class);
    }

    @Override
    protected boolean isForViewType(@NonNull List<Events> items, int position) {
        // Return true since we only have one Event type for now
        // Eventually we can assign this to return true only for "Top Pick" events or something
        if(items.get(position).getHeader()!=null)
            return true;
        return false;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.listitem_header, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(
            @NonNull List<Events> items,
            int position,
            @NonNull RecyclerView.ViewHolder holder,
            @NonNull List<Object> payloads) {

        EventViewHolder viewHolder = (EventViewHolder) holder;
        final Events event = items.get(position);

        viewHolder.title.setText(DateCostFormatter.formatDatefromDate(items.get(position).getHeader()));
    }

    /**
     * ViewHolder to store references to the Layout Views for use when binding
     */
    public static class EventViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.header_date_time)
        public TextView title;


        public EventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
