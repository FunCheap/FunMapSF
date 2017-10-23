package com.funcheap.funmapsf.features.list.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.commons.models.Events;
import com.funcheap.funmapsf.features.detail.DetailActivity;
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.funcheap.funmapsf.R.id.ivBackdrop;
import static com.funcheap.funmapsf.R.id.ivItemImg;

/**
 * Created by Jayson on 10/13/2017.
 * <p>
 * Adapter Delegate that handles binding normal Event items to a RecyclerView.
 */

public class EventDelegateAdapter extends AdapterDelegate<List<Events>> {

    private LayoutInflater mInflater;
    private Events mEvent;
    private Context mContext;
    private static final String EVENT_EXTRA = "event_extra";

    public EventDelegateAdapter(Activity activity) {
        this.mInflater = activity.getLayoutInflater();
        this.mContext = activity.getApplicationContext();
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
        View view = mInflater.inflate(R.layout.listitem_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(
            @NonNull List<Events> items,
            int position,
            @NonNull RecyclerView.ViewHolder holder,
            @NonNull List<Object> payloads) {

        EventViewHolder viewHolder = (EventViewHolder) holder;
        mEvent = items.get(position);

        viewHolder.title.setText(items.get(position).getTitle());
        viewHolder.dateTime.setText(items.get(position).getStartDate());
        viewHolder.price.setText(items.get(position).getCost());
        viewHolder.venue.setText(items.get(position).getVenue().getVenueAddress());

        Picasso.Builder builder = new Picasso.Builder(mContext);
        builder.indicatorsEnabled(true);
        builder.listener(new Picasso.Listener()
        {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
            {
                exception.printStackTrace();
            }
        });
        builder.build().load(mEvent.getThumbnail()).fit().centerCrop().into(viewHolder.ivItemImg);

        holder.itemView.setOnClickListener(myView -> {
            if (mEvent != null)
            {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(EVENT_EXTRA, items.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * ViewHolder to store references to the Layout Views for use when binding
     */
    public static class EventViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_title)
        public TextView title;
        @BindView(R.id.text_date_time)
        public TextView dateTime;
        @BindView(R.id.text_price)
        public TextView price;
        @BindView(R.id.text_venue)
        public TextView venue;
        @BindView(R.id.ivItemImg)
        public ImageView ivItemImg;

        public EventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
