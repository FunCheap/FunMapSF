package com.funcheap.funmapsf.features.detail;

import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.commons.models.Events;
import com.funcheap.funmapsf.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String EVENT_EXTRA_ID = "event_extra_id";
    private static final String EVENT_EXTRA = "event_extra";
    private final String TAG = this.getClass().getSimpleName();

    private static final String EVENT_TYPE = "vnd.android.cursor.item/event";
    private static final String EVENT_BEGIN_TIME = "beginTime";
    private static final String EVENT_END_TIME = "endTime";
    private static final String EVENT_ALL_DAY = "allDay";
    private static final String EVENT_TITLE = "title";
    private static final String EVENT_LOCATION = "eventLocation";

    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsing_toolbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ivBackdrop)
    ImageView ivBackdrop;

    private DetailViewModel mDetailViewModel;
    private ActivityDetailBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_detail);
        ButterKnife.bind(this);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDetailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);

        initEvent();
        initToolbar();
    }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Gets an intent passed as an extra. If not, attempt to get an EventID passed through an
     * extra and obtain the corresponding event.
     */
    private void initEvent() {
        Intent intent = getIntent();

        if (intent.hasExtra(EVENT_EXTRA)) {
            // Get event directly
            Events event = getIntent().getExtras().getParcelable(EVENT_EXTRA);
            mDetailViewModel.setEventData(event);
            mDetailViewModel.getEventData().observe(this, events -> mBinding.setEvents(events));
            setImage();
        } else if (intent.hasExtra(EVENT_EXTRA_ID)) {
            // Get event by ID
            String id = getIntent().getStringExtra(EVENT_EXTRA_ID);
            mDetailViewModel.getEventById(id).observe(this, events -> mBinding.setEvents(events));
        } else {
            Log.d(TAG, "initEvent: No valid Event data was given!");
        }
    }

    public void setImage()
    {

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.indicatorsEnabled(true);
        builder.listener((picasso, uri, exception) -> exception.printStackTrace());
        builder.build().load(mDetailViewModel.getEventData().getValue().getThumbnail()).fit().centerCrop().into(ivBackdrop);


    }

    public void onCalenderClick(View v) {
        Toast.makeText(getApplicationContext(), "onCalenderClick", Toast.LENGTH_SHORT).show();

        try
        {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("PDT"));
        Date startDate =   sdf.parse(mDetailViewModel.getEventData().getValue().getStartDate());
        Date endDate =   sdf.parse(mDetailViewModel.getEventData().getValue().getEndDate());
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType(EVENT_TYPE);
        intent.putExtra(EVENT_BEGIN_TIME, startDate.getTime());
        intent.putExtra(EVENT_ALL_DAY, false);
        intent.putExtra(EVENT_END_TIME, endDate.getTime());
        intent.putExtra(EVENT_TITLE, mDetailViewModel.getEventData().getValue().getTitle());
        intent.putExtra(EVENT_LOCATION, mDetailViewModel.getEventData().getValue().getVenue().getVenueAddress());
        startActivity(intent);
        }
        catch (Exception ex)
        {

        }
    }

    public void onDirectionsClick(View v) {
        Toast.makeText(getApplicationContext(), "onDirectionsClick", Toast.LENGTH_SHORT).show();
        String uri = "http://maps.google.com/maps?daddr=" +
                mDetailViewModel.getEventData().getValue().getVenue().getLatitude()+
                ","+
                mDetailViewModel.getEventData().getValue().getVenue().getLongitude()
                ;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    public void onShareClick(View v) {
        Toast.makeText(getApplicationContext(), "onShareClick", Toast.LENGTH_SHORT).show();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        ArrayList<String> extraList = new ArrayList<String>();
        extraList.add(mDetailViewModel.getEventData().getValue().getTitle());
        extraList.add(mDetailViewModel.getEventData().getValue().getStartDate());
        extraList.add(mDetailViewModel.getEventData().getValue().getPermalink());
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mDetailViewModel.getEventData().getValue().getPermalink());
        startActivity(Intent.createChooser(shareIntent, "Share link using"));
    }

    public void onSaveClick(View v) {
        Toast.makeText(getApplicationContext(), "onSaveClick", Toast.LENGTH_SHORT).show();
        Events event = mDetailViewModel.getEventData().getValue();
        if (event != null) {
            event.setBookmark(!event.isBookmarked());
            event.save();
            if (event.isBookmarked()) {
                Toast.makeText(this, "Event bookmarked!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Event un-bookmarked!", Toast.LENGTH_LONG).show();
            }
        }

        // TODO Visually change icon to show bookmarked or un-bookmarked
    }

    public void onLinkClick(View v)
    {
        Toast.makeText(getApplicationContext(), "onLinkClick", Toast.LENGTH_SHORT).show();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_share_black);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, mDetailViewModel.getEventData().getValue().getPermalink());
        int requestCode = 100;

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.primary_dark));
        builder.setActionButton(bitmap, "Share Link", pendingIntent, true);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(mDetailViewModel.getEventData().getValue().getPermalink()));
    }


}
