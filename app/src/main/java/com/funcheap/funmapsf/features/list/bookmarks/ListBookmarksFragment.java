package com.funcheap.funmapsf.features.list.bookmarks;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funcheap.funmapsf.commons.models.Events;
import com.funcheap.funmapsf.features.list.ListBaseFragment;

import java.util.List;

/**
 * Created by Jayson on 10/11/2017.
 *
 * Fragment for displaying a list of bookmarked events. It should display
 * a list of all events the current user has bookmarked and ignore events that happened in
 * the past.
 *
 * OPTIONAL: Show past events under a separate header, with a "greyed-out" style
 */

public class ListBookmarksFragment extends ListBaseFragment {

    ListBookmarksViewModel mListBookmarksViewModel;

    public static Fragment newInstance() {
        Bundle args = new Bundle();
        // Set any input args here
        ListBookmarksFragment fragment = new ListBookmarksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Get a reference to our ViewModel
        mListBookmarksViewModel = ViewModelProviders.of(this).get(ListBookmarksViewModel.class);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected LiveData<List<Events>> getEventData() {
        // Bind data from ListBookmarksViewModel to our adapter via the onEventsChanged method
        return mListBookmarksViewModel.getBookmarksEventsData();
    }
}
