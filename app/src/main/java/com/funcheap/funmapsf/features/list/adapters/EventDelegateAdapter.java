package com.funcheap.funmapsf.features.list.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.commons.models.Events;
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jayson on 10/13/2017.
 *
 * Adapter Delegate that handles binding normal Event items to a RecyclerView.
 */

public class EventDelegateAdapter extends AdapterDelegate<List<Events>> {

    private LayoutInflater mInflater;

    public EventDelegateAdapter(Activity activity) {
        this.mInflater = activity.getLayoutInflater();
    }

    @Override
    protected boolean isForViewType(@NonNull List<Events> items, int position) {
        // Return true since we only have one Event type for now
        // Eventually we can assign this to return true only for "Top Pick" events or something
        return true;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new EventViewHolder(mInflater.inflate(R.layout.listitem_event, parent, false));
    }

    @Override
    protected void onBindViewHolder(
            @NonNull List<Events> items,
            int position,
            @NonNull RecyclerView.ViewHolder holder,
            @NonNull List<Object> payloads) {

        EventViewHolder viewHolder = (EventViewHolder) holder;

        viewHolder.text.setText(items.get(position).getTitle());
    }

    /**
     * ViewHolder to store references to the Layout Views for use when binding
     */
    public static class EventViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_title)
        public TextView text;

        public EventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
