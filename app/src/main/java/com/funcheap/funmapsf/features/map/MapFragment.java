package com.funcheap.funmapsf.features.map;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funcheap.funmapsf.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Jayson on 10/11/2017.
 *
 * This is the fragment used to display our MapView.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private final String TAG = this.getClass().getSimpleName();

    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private MapsViewModel mMapsViewModel;

    public static MapFragment newInstance() {
        Bundle args = new Bundle();
        // Set any input args here
        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find our MapFragment and be notified when the map is ready to be used.
        mMapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        mMapFragment.getMapAsync(this);

        // Get a reference to our ViewModel shared by our Activity
        mMapsViewModel = ViewModelProviders.of(getActivity()).get(MapsViewModel.class);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        initEvents();

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    /**
     * Get events {@link android.arch.lifecycle.LiveData} from our ViewModel and observe
     * changes. On change, populate the map.
     */
    private void initEvents() {
        mMapsViewModel.getEventsData().observe(this, eventsList -> {
            // Add markers from eventsList here
        });
    }
}
