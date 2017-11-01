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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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

public class EventDelegateAdapter extends AdapterDelegate<List<Events>> {

    private LayoutInflater mInflater;
    private Context mContext;
    private ListBaseViewModel mListBaseViewModel;
    private final String TAG = this.getClass().getSimpleName();
    private static final String EVENT_EXTRA = "event_extra";

    public EventDelegateAdapter(Activity activity) {
        this.mInflater = activity.getLayoutInflater();
        this.mContext = activity;
        mListBaseViewModel = ViewModelProviders.of((FragmentActivity) activity).get(ListBaseViewModel.class);
    }

    @Override
    protected boolean isForViewType(@NonNull List<Events> items, int position) {
        // Return true since we only have one Event type for now
        // Eventually we can assign this to return true only for "Top Pick" events or something
        if(items.get(position).getHeader()==null)
            return true;
        return false;
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
        final Events event = items.get(position);

        viewHolder.title.setText(items.get(position).getTitle());
        viewHolder.dateTime.setText(DateCostFormatter.formatDate(items.get(position).getStartDate()));
        viewHolder.price.setText(DateCostFormatter.formatCost(items.get(position).getCost()));
        viewHolder.venue.setText(items.get(position).getVenue().getVenueAddress());

        // Setup bookmark
        final Drawable bookmark = mContext.getDrawable(R.drawable.ic_bookmark);
        final Drawable bookmarkOutline = mContext.getDrawable(R.drawable.ic_bookmark_outline);
        if (event.isBookmarked()) {
            viewHolder.imgBookmark.setImageDrawable(bookmark);
            viewHolder.imgBookmark.setColorFilter(mContext.getResources().getColor(R.color.light_blue));
        } else {
            viewHolder.imgBookmark.setImageDrawable(bookmarkOutline);
            viewHolder.imgBookmark.setColorFilter(mContext.getResources().getColor(R.color.icon_tint_gray));
        }

        viewHolder.imgBookmark.setOnClickListener( view -> {
            event.setBookmark(!event.isBookmarked());
            event.save();

            if (event.isBookmarked()) {
                Log.d(TAG, "onBindViewHolder: EventID bookmarked - " + event.getEventId());
                viewHolder.imgBookmark.setImageDrawable(bookmark);
                viewHolder.imgBookmark.setColorFilter(mContext.getResources().getColor(R.color.light_blue));
            } else {
                Log.d(TAG, "onBindViewHolder: EventID un-bookmarked - " + event.getEventId());
                viewHolder.imgBookmark.setImageDrawable(bookmarkOutline);
                viewHolder.imgBookmark.setColorFilter(mContext.getResources().getColor(R.color.icon_tint_gray));
            }
        });

        // Load Image
        Glide.with(mContext).load(event.getThumbnail())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(viewHolder.ivItemImage);


        holder.itemView.setOnClickListener(myView -> {
            if (event != null)
            {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(EVENT_EXTRA, items.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) mContext, viewHolder.imgView, "profile");
                mContext.startActivity(intent, options.toBundle());
            }
        });

        animate(holder);
    }

    private void animate(RecyclerView.ViewHolder holder)
    {
        YoYo.with(Techniques.FadeIn)
                .duration(500)
                .playOn(holder.itemView);
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
