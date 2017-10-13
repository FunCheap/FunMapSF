package com.funcheap.funmapsf.features.list.home;

import android.os.Bundle;

import com.funcheap.funmapsf.features.list.ListBaseFragment;

/**
 * Created by Jayson on 10/11/2017.
 *
 * Fragment for displaying a list of all events on the home screen. It should
 * display a list of events that correspond to the currently visible events in the Home/MapFragment
 */

public class ListHomeFragment extends ListBaseFragment {

    public static ListHomeFragment newInstance(){
        Bundle args = new Bundle();
        // Set any input args here
        ListHomeFragment fragment = new ListHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
