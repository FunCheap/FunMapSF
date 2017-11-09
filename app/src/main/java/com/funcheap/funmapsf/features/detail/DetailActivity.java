package com.funcheap.funmapsf.features.detail;

import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.commons.models.Events;
import com.funcheap.funmapsf.commons.utils.ChipUtils;
import com.funcheap.funmapsf.commons.utils.DateCostFormatter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.vpaliy.chips_lover.ChipView;

import org.apmem.tools.layouts.FlowLayout;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

    private GoogleMap m_map;

    @BindView(R.id.appbar_detail)
    AppBarLayout appbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsing_toolbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvEventName)
    TextView tvEventName;
    @BindView(R.id.tvEventAddress)
    TextView tvEventAddress;
    @BindView(R.id.tvMapAddress)
    TextView tvMapAddress;
    @BindView(R.id.tvEventDate)
    TextView tvEventDate;
    @BindView(R.id.tvEventCost)
    TextView tvEventCost;
    @BindView(R.id.ivBookmark)
    ImageView ivBookmark;
    @BindView(R.id.llEventCategories)
    FlowLayout clEventCategories;
    @BindView(R.id.ivBackdrop)
    ImageView ivBackdrop;
    @BindView(R.id.tvContent)
    HtmlTextView tvContent;
    @BindView(R.id.map_card)
    ConstraintLayout mapLayout;

    private DetailViewModel mDetailViewModel;

    private Events mEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        mDetailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);

        initEvent();
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
            mDetailViewModel.getEventData().observe(this, this::bindEvent);
        } else if (intent.hasExtra(EVENT_EXTRA_ID)) {
            // Get event by ID
            String id = getIntent().getStringExtra(EVENT_EXTRA_ID);
            mDetailViewModel.getEventById(id).observe(this, this::bindEvent);
        } else {
            Log.d(TAG, "initEvent: No valid Event data was given!");
        }
    }

    public void bindEvent(Events event) {
        // Store our event locally
        this.mEvent = event;

        initToolbar();

        tvEventName.setText(mEvent.getTitle());

        tvEventAddress.setText(mEvent.getVenue().getVenueAddress());
        tvMapAddress.setText(mEvent.getVenue().getVenueAddress());


        // Load Image
        Glide.with(this).load(mEvent.getThumbnail())
                .into(ivBackdrop);
        tvEventDate.setText(DateCostFormatter.formatDate(mEvent.getStartDate()));
        tvEventCost.setText(DateCostFormatter.formatCost(mEvent.getCost()));
        tvContent.setHtml(DateCostFormatter.formatContent(mEvent.getContent()));

        initBookmark();
        initCategories();
        initMaps();
    }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Set title only when appbar is collapsed
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsing_toolbar.setTitle(mEvent.getTitle());
                    isShow = true;
                } else if (isShow) {
                    collapsing_toolbar.setTitle("");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initCategories() {
        for (String s : mEvent.getCategoriesList()) {
            ChipView chip = ChipUtils.createSimpleChip(s);
            clEventCategories.addView(chip);
            ((ViewGroup.MarginLayoutParams) chip.getLayoutParams())
                    .setMargins(0, 0, 20, 20);
        }
    }

    private void initBookmark() {
        // Setup bookmark
        final Drawable bookmark = this.getDrawable(R.drawable.ic_bookmark);
        final Drawable bookmarkOutline = this.getDrawable(R.drawable.ic_bookmark_outline);
        if (mEvent.isBookmarked()) {
            ivBookmark.setImageDrawable(bookmark);
            ivBookmark.setColorFilter(this.getResources().getColor(R.color.light_blue));

        } else {
            ivBookmark.setImageDrawable(bookmarkOutline);
            ivBookmark.setColorFilter(this.getResources().getColor(R.color.icon_tint_gray));
        }
    }

    public void onCalenderClick(View v) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            Date startDate = sdf.parse(mEvent.getStartDate());
            Date endDate = sdf.parse(mEvent.getEndDate());
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setType(EVENT_TYPE);
            intent.putExtra(EVENT_BEGIN_TIME, startDate.getTime());
            intent.putExtra(EVENT_ALL_DAY, false);
            intent.putExtra(EVENT_END_TIME, endDate.getTime());
            intent.putExtra(EVENT_TITLE, mEvent.getTitle());
            intent.putExtra(EVENT_LOCATION, mEvent.getVenue().getVenueAddress());
            startActivity(intent);
        } catch (Exception ex) {
        }
    }

    public void onDirectionsClick(View v) {
        String uri = "http://maps.google.com/maps?daddr=" +
                mEvent.getVenue().getLatitude() +
                "," +
                mEvent.getVenue().getLongitude();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    public void onShareClick(View v) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        ArrayList<String> extraList = new ArrayList<String>();
        extraList.add(mEvent.getTitle());
        extraList.add(mEvent.getStartDate());
        extraList.add(mEvent.getPermalink());
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mEvent.getPermalink());
        startActivity(Intent.createChooser(shareIntent, "Share link using"));
    }

    public void onSaveClick(View v) {
        mEvent.setBookmark(!mEvent.isBookmarked());
        mEvent.save();

        // Update Bookmark View
        final Drawable bookmark = this.getDrawable(R.drawable.ic_bookmark);
        final Drawable bookmarkOutline = this.getDrawable(R.drawable.ic_bookmark_outline);
        if (mEvent.isBookmarked()) {
            ivBookmark.setImageDrawable(bookmark);
            ivBookmark.setColorFilter(this.getResources().getColor(R.color.light_blue));
        } else {
            ivBookmark.setImageDrawable(bookmarkOutline);
            ivBookmark.setColorFilter(this.getResources().getColor(R.color.icon_tint_gray));
        }
    }

    public void onLinkClick(View v) {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_share_black);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, mEvent.getPermalink());
        int requestCode = 100;

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.primary_dark));
        builder.setActionButton(bitmap, "Share Link", pendingIntent, true);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(mEvent.getPermalink()));
    }

    public void initMaps() {
        SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));

        if (mapFragment != null) {
            mapFragment.getMapAsync(map -> {
                m_map = map;
                m_map.getUiSettings().setAllGesturesEnabled(false);

                LatLng point = new LatLng(Double.parseDouble(mEvent.getVenue().getLatitude()),
                        Double.parseDouble(mEvent.getVenue().getLongitude()));
                m_map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));

                // Creates and adds circle to the map
                m_map.addCircle(new CircleOptions()
                        .center(point)
                        .strokeColor(ContextCompat.getColor(this, R.color.primary_light))
                        .fillColor(ContextCompat.getColor(this, R.color.primary))
                        .radius(50));

                m_map.setOnMapClickListener(latLng -> onDirectionsClick(null));
            });
        } else {
            Toast.makeText(this, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean openLyftApp() {
        PackageManager manager = this.getPackageManager();
        Intent i = manager.getLaunchIntentForPackage("me.lyft.android");
        if (i == null) {
            return false;
        }
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        this.startActivity(i);
        return true;
    }
}
