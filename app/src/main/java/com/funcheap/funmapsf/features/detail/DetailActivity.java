package com.funcheap.funmapsf.features.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.commons.models.Events;
import com.funcheap.funmapsf.databinding.ActivityDetailBinding;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static final String EVENT_EXTRA_ID = "event_extra_id";
    private static final String EVENT_EXTRA = "event_extra";
    private final String TAG = this.getClass().getSimpleName();

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
        } else if (intent.hasExtra(EVENT_EXTRA_ID)) {
            // Get event by ID
            long id = Long.parseLong(getIntent().getStringExtra(EVENT_EXTRA_ID));
            mDetailViewModel.getEventById(id).observe(this, events -> mBinding.setEvents(events));
        } else {
            Log.d(TAG, "initEvent: No valid Event data was given!");
        }
    }

    public void onCalenderClick(View v) {
        Toast.makeText(getApplicationContext(), "onCalenderClick", Toast.LENGTH_SHORT).show();
        showDate();
    }

    public void onDirectionsClick(View v) {
        Toast.makeText(getApplicationContext(), "onDirectionsClick", Toast.LENGTH_SHORT).show();
        String uri = "http://maps.google.com/maps?daddr=" + "37.8199 N,122.4483 W";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    public void onShareClick(View v) {
        Toast.makeText(getApplicationContext(), "onShareClick", Toast.LENGTH_SHORT).show();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mDetailViewModel.getEventData().getValue().getTitle());
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

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minute) {
        //   setTime(hour, minute);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        //   setDate(year, month, day);
        showTime();
    }

    public void showDate() {
        Date date = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(DetailActivity.this, year, month, day);

        datePickerDialog.show(getFragmentManager(), "DateFragment");
    }

    public void showTime() {
        Date date = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(DetailActivity.this, hour, minute, DateFormat.is24HourFormat(getApplicationContext()));

        timePickerDialog.show(getFragmentManager(), "TimeFragment");
    }
}
