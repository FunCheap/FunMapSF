package com.funcheap.funmapsf.features.list.adapters;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.commons.models.Events;
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

public class EventDelegateAdapter extends AdapterDelegate<List<Events>> {

    private LayoutInflater mInflater;
    private Events mEvent;
    private Context mContext;
    private ListBaseViewModel mListBaseViewModel;
    private static final String EVENT_EXTRA = "event_extra";

    public EventDelegateAdapter(Activity activity) {
        this.mInflater = activity.getLayoutInflater();
    //    this.mContext = activity.getApplicationContext();
        this.mContext = activity;
        mListBaseViewModel = ViewModelProviders.of((FragmentActivity) activity).get(ListBaseViewModel.class);
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

        viewHolder.imgBookmark.setOnClickListener( view -> {
            Events event = items.get(position);
            event.setBookmark(!event.isBookmarked());
            event.save();

            if (event.isBookmarked()) {
                Toast.makeText(mContext, "Event bookmarked!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext, "Event un-bookmarked!", Toast.LENGTH_LONG).show();
            }

            // TODO Visually change icon to show bookmarked or un-bookmarked
        });

        // Load Image
        Glide.with(mContext).load(mEvent.getThumbnail())
                .into(viewHolder.ivItemImage);


        holder.itemView.setOnClickListener(myView -> {
            if (mEvent != null)
            {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(EVENT_EXTRA, items.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) mContext, viewHolder.imgView, "profile");
                mContext.startActivity(intent, options.toBundle());
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
        @BindView(R.id.img_bookmark)
        public ImageView imgBookmark;
        @BindView(ivItemImg)
        public ImageView ivItemImage;
        public View imgView;

        public EventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            imgView = itemView.findViewById(R.id.ivItemImg);
        }
    }

}
