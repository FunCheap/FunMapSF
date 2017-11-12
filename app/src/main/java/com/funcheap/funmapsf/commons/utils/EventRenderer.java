package com.funcheap.funmapsf.commons.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
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
        mClusterIconGenerator = new IconGenerator(context);
        mClusterIconGenerator.setBackground(mCtx.getResources().getDrawable(R.drawable.maps_cluster_background));
        mClusterIconGenerator.setTextAppearance(mCtx,R.style.AppTheme_WhiteTextAppearance);

    }

    @Override
    protected void onBeforeClusterItemRendered(Events event, MarkerOptions markerOptions) {
        LayerDrawable drawable = (LayerDrawable) mCtx.getResources().getDrawable(R.drawable.maps_icon_background);
        Drawable change_icon;
        if(event.getCategories().contains("Top"))
            change_icon = mCtx.getResources().getDrawable(R.drawable.ic_star_white);
        else if(event.getCategories().contains("Art"))
            change_icon = mCtx.getResources().getDrawable(R.drawable.ic_arts_white);
        else if(event.getCategories().contains("Charity"))
            change_icon = mCtx.getResources().getDrawable(R.drawable.ic_charity_white);
        else if(event.getCategories().contains("Club"))
            change_icon = mCtx.getResources().getDrawable(R.drawable.ic_club_white);
        else if(event.getCategories().contains("Comedy"))
            change_icon = mCtx.getResources().getDrawable(R.drawable.ic_comedy_white);
        else if(event.getCategories().contains("Eating"))
            change_icon = mCtx.getResources().getDrawable(R.drawable.ic_eating_white);
        else if(event.getCategories().contains("Fairs"))
            change_icon = mCtx.getResources().getDrawable(R.drawable.ic_fair_white);
        else if(event.getCategories().contains("Free Stuff"))
            change_icon = mCtx.getResources().getDrawable(R.drawable.ic_free_white);
        else if(event.getCategories().contains("Games"))
            change_icon = mCtx.getResources().getDrawable(R.drawable.ic_games_white);
        else if(event.getCategories().contains("Geek"))
            change_icon = mCtx.getResources().getDrawable(R.drawable.ic_geek_white);
        else if(event.getCategories().contains("Families"))
            change_icon = mCtx.getResources().getDrawable(R.drawable.ic_family_white);
        else if(event.getCategories().contains("Lectures"))
            change_icon = mCtx.getResources().getDrawable(R.drawable.ic_lecture_white);
        else if(event.getCategories().contains("Literature"))
            change_icon = mCtx.getResources().getDrawable(R.drawable.ic_literature_white);
        else if(event.getCategories().contains("Live Music"))
            change_icon = mCtx.getResources().getDrawable(R.drawable.ic_music_white);
        else if(event.getCategories().contains("Movies"))
            change_icon = mCtx.getResources().getDrawable(R.drawable.ic_movie_white);
        else if(event.getCategories().contains("Shopping"))
            change_icon = mCtx.getResources().getDrawable(R.drawable.ic_shopping_white);
        else if(event.getCategories().contains("Sports"))
            change_icon = mCtx.getResources().getDrawable(R.drawable.ic_sports_white);
        else if(event.getCategories().contains("Theater"))
            change_icon = mCtx.getResources().getDrawable(R.drawable.ic_theater_white);
        else
            change_icon = mCtx.getResources().getDrawable(R.drawable.ic_star_white);


        drawable.setDrawableByLayerId(R.id.icon_drawable, change_icon);
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