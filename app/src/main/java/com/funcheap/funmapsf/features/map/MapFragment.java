package com.funcheap.funmapsf.features.map;

import android.Manifest;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.commons.models.Events;
import com.funcheap.funmapsf.commons.utils.EventRenderer;
import com.funcheap.funmapsf.features.detail.DetailActivity;
import com.funcheap.funmapsf.features.list.cluster.ListClusterActivity;
import com.funcheap.funmapsf.features.list.cluster.ListClusterFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

/**
 * Created by Jayson on 10/11/2017.
 * <p>
 * This is the fragment used to display our MapView.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback,
        ClusterManager.OnClusterClickListener<Events>,
        ClusterManager.OnClusterInfoWindowClickListener<Events>,
        ClusterManager.OnClusterItemClickListener<Events>,
        ClusterManager.OnClusterItemInfoWindowClickListener<Events> {

    private final String TAG = this.getClass().getSimpleName();
    private static final String EVENT_EXTRA = "event_extra";
    private static final int MY_REQUEST_CODE = 1;

    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private MapsViewModel mMapsViewModel;
    private ClusterManager<Events> mClusterManager;
    private Context mCtx;
    ProgressDialog dialog;

    public static MapFragment newInstance() {
        Bundle args = new Bundle();
        // Set any input args here
        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCtx = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        dialog = new ProgressDialog(mCtx);
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
        if (mMap != null) {
            return;
        }
        mMap = googleMap;
        if(checkPermission()) {
            getMap().setMyLocationEnabled(true);
            getMap().getUiSettings().setMyLocationButtonEnabled(true);

        }
        startDemo();
        initEvents();

    }

    @Override
    public boolean onClusterClick(Cluster<Events> cluster) {

        Intent intent = new Intent(mCtx, ListClusterActivity.class);
        intent.putParcelableArrayListExtra(
                ListClusterFragment.EVENT_LIST_EXTRA, new ArrayList<>(cluster.getItems()));
        startActivity(intent);

        Log.i("Cluster", "onClusterClick " + cluster.getItems().size());
        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<Events> cluster) {
        // Does nothing, but you could go to a list of the users.
    }

    @Override
    public boolean onClusterItemClick(Events event) {
        Intent intent = new Intent();
        intent.setClass(mCtx, DetailActivity.class);
        intent.putExtra(EVENT_EXTRA, event);
        startActivity(intent);
        return true;
    }

    @Override
    public void onClusterItemInfoWindowClick(Events event) {

    }

    /**
     * Get events {@link android.arch.lifecycle.LiveData} from our ViewModel and observe
     * changes. On change, populate the map.
     */
    private void initEvents() {
        mMapsViewModel.getEventsData().observe(this, eventsList -> {
            // Add markers from updated eventsList
            mMapsViewModel.setLoading(false);

            mClusterManager.clearItems();
            mClusterManager.addItems(eventsList);
            // Setting the zoom level back and default Location to San Francisco
            getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.7749, -122.4194), 11));
            mClusterManager.setRenderer(new EventRenderer(mCtx, getMap(), mClusterManager));
            mClusterManager.cluster();

            if (eventsList != null && eventsList.isEmpty()) {
                Toast.makeText(mCtx, "0 events found!", Toast.LENGTH_LONG).show();
            }
        });
        mMapsViewModel.isLoading().observe(this, isLoading -> processLoadingStatus(isLoading));
    }

    private void processLoadingStatus(boolean isLoading) {
        if(isLoading) {
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.progress_dialog);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        }
        else
            dialog.dismiss();
    }

    private void setListeners() {
        getMap().setOnCameraChangeListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        getMap().setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);


    }

    private void setMapUISettings() {
        getMap().getUiSettings().setZoomControlsEnabled(false);
        getMap().getUiSettings().setRotateGesturesEnabled(false);
        getMap().getUiSettings().setScrollGesturesEnabled(true);
        getMap().getUiSettings().setTiltGesturesEnabled(false);
        getMap().setPadding(0, 0, 0, 210);

    }
    private boolean checkPermission() {
        if ((ActivityCompat.checkSelfPermission(mCtx, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED )||(ActivityCompat.checkSelfPermission(mCtx, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED ))
            return true;

        requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, MY_REQUEST_CODE);
        return false;
    }

    private GoogleMap getMap() {
        return mMap;
    }

    protected void startDemo() {
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.7749, -122.4194), 10));
        mClusterManager = new ClusterManager<>(mCtx, getMap());
        setListeners();
        setMapUISettings();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MY_REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(checkPermission()) {
                    getMap().setMyLocationEnabled(true);
                    getMap().getUiSettings().setMyLocationButtonEnabled(true);
                }
                else {
                    Toast.makeText(mCtx, "Current location is disabled", Toast.LENGTH_LONG).show();
                    getMap().setMyLocationEnabled(false);
                    getMap().getUiSettings().setMyLocationButtonEnabled(false);
                }
            }
            else{
                getMap().setMyLocationEnabled(false);
                getMap().getUiSettings().setMyLocationButtonEnabled(false);
                Toast.makeText(mCtx, "Current location is disabled", Toast.LENGTH_LONG).show();
            }
        }
    }
}
