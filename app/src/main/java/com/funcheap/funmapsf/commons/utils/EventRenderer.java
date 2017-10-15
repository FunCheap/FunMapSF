package com.funcheap.funmapsf.commons.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.funcheap.funmapsf.commons.models.Events;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

public class EventRenderer extends DefaultClusterRenderer<Events> {
    private final IconGenerator mIconGenerator;
    private final IconGenerator mClusterIconGenerator;

    Context mCtx;
    ClusterManager<Events> mClusterManager;
    GoogleMap map;

    public EventRenderer(Context context, GoogleMap map, ClusterManager<Events> clusterManager) {
        super(context, map, clusterManager);

        mCtx = context;
        mClusterManager = clusterManager;
        this.map = map;

        mIconGenerator = new IconGenerator(context);
        mClusterIconGenerator = new IconGenerator(context);

    }

    @Override
    protected void onBeforeClusterItemRendered(Events event, MarkerOptions markerOptions) {
        Bitmap icon = mIconGenerator.makeIcon("1");
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(event.getTitle());
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<Events> cluster, MarkerOptions markerOptions) {
        Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        // Always render clusters.
        return cluster.getSize() > 1;
    }
}