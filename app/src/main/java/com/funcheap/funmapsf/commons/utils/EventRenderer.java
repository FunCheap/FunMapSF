package com.funcheap.funmapsf.commons.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.graphics.drawable.BitmapDrawable;

import com.funcheap.funmapsf.R;
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
    LayoutInflater inflater;
    View itemView;

    public EventRenderer(Context context, GoogleMap map, ClusterManager<Events> clusterManager) {
        super(context, map, clusterManager);

        mCtx = context;
        mClusterManager = clusterManager;
        this.map = map;

        mIconGenerator = new IconGenerator(context);
        mClusterIconGenerator = new IconGenerator(context);
        mIconGenerator.setBackground(mCtx.getResources().getDrawable(R.drawable.maps_icon_background));
        mIconGenerator.setTextAppearance(mCtx,R.style.AppTheme_WhiteTextAppearance);
        mClusterIconGenerator.setBackground(mCtx.getResources().getDrawable(R.drawable.maps_cluster_background));
        mClusterIconGenerator.setTextAppearance(mCtx,R.style.AppTheme_WhiteTextAppearance);

    }

    @Override
    protected void onBeforeClusterItemRendered(Events event, MarkerOptions markerOptions) {
        Drawable drawable = mCtx.getResources().getDrawable(R.drawable.maps_icon_background);
        Bitmap marker = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888 );
        Canvas canvas = new Canvas(marker);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(marker)).title(event.getTitle());
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<Events> cluster, MarkerOptions markerOptions) {
        inflater = LayoutInflater.from(mCtx);
        itemView = inflater.inflate(R.layout.cluster_view,null);

        mClusterIconGenerator.setContentView(itemView);
        Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        // Always render clusters.
        return cluster.getSize() > 1;
    }
}