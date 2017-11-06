package com.funcheap.funmapsf.commons.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;

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
        BitmapDrawable bitmapdraw;

        if(event.getCategories().contains("Comedy"))
            bitmapdraw=(BitmapDrawable)mCtx.getResources().getDrawable(R.drawable.lol);
        else if(event.getCategories().contains("Game"))
            bitmapdraw=(BitmapDrawable)mCtx.getResources().getDrawable(R.drawable.games);
        else if(event.getCategories().contains("Eating"))
            bitmapdraw=(BitmapDrawable)mCtx.getResources().getDrawable(R.drawable.eating);
        else if(event.getCategories().contains("Art"))
            bitmapdraw=(BitmapDrawable)mCtx.getResources().getDrawable(R.drawable.arts);
        else if(event.getCategories().contains("Theater"))
            bitmapdraw=(BitmapDrawable)mCtx.getResources().getDrawable(R.drawable.theater);
        else if(event.getCategories().contains("Families"))
            bitmapdraw=(BitmapDrawable)mCtx.getResources().getDrawable(R.drawable.family);
        else if(event.getCategories().contains("Shopping"))
            bitmapdraw=(BitmapDrawable)mCtx.getResources().getDrawable(R.drawable.shopping);
        else if(event.getCategories().contains("Music"))
            bitmapdraw=(BitmapDrawable)mCtx.getResources().getDrawable(R.drawable.music);
        else if(event.getCategories().contains("Club"))
            bitmapdraw=(BitmapDrawable)mCtx.getResources().getDrawable(R.drawable.club);
        else if(event.getCategories().contains("Movie"))
            bitmapdraw=(BitmapDrawable)mCtx.getResources().getDrawable(R.drawable.movie);
        else if(event.getCategories().contains("Sports"))
            bitmapdraw=(BitmapDrawable)mCtx.getResources().getDrawable(R.drawable.sports);
        else if(event.getCategories().contains("Top"))
            bitmapdraw=(BitmapDrawable)mCtx.getResources().getDrawable(R.drawable.topick);
        else
            bitmapdraw=(BitmapDrawable)mCtx.getResources().getDrawable(R.drawable.party_trans);

        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).title(event.getTitle());
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