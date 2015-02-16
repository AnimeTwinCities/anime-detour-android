/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2014-2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.animedetour.android.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;

import static com.animedetour.android.map.HotelMapPoints.HOTEL_CENTER;

/**
 * Google Maps display with Hotel floor plans overlayed.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
final public class HotelMapFragment extends SupportMapFragment
{
    @InjectView(R.id.map_control_first_floor)
    Button switchFirstFloor;

    @InjectView(R.id.map_control_second_floor)
    Button switchSecondFloor;

    @InjectView(R.id.map_control_22nd_floor)
    Button switch22ndFloor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View mapView = super.onCreateView(inflater, container, savedInstanceState);
        View controlView = inflater.inflate(R.layout.map_controls, container, false);
        FrameLayout composite = new FrameLayout(this.getActivity());
        composite.addView(mapView);
        composite.addView(controlView);

        return composite;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.inject(this, view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        this.resetMap();
        this.centerMap();
        this.showFirstFloor();
    }

    /**
     * Center the map camera on the hotel and zoom appropriately.
     */
    private void centerMap()
    {
        GoogleMap map = this.getMap();
        CameraUpdate camera = CameraUpdateFactory.newCameraPosition(
            new CameraPosition.Builder().target(HOTEL_CENTER).zoom(18F).tilt(0).bearing(180).build()
        );
        map.moveCamera(camera);
    }

    /**
     * Reset any overlays from the map so we can draw new floors.
     */
    private void resetMap()
    {
        GoogleMap map = this.getMap();
        map.clear();
        map.setBuildingsEnabled(true);
        map.setIndoorEnabled(false);
        map.getUiSettings().setTiltGesturesEnabled(false);
        map.getUiSettings().setRotateGesturesEnabled(false);
        map.getUiSettings().setCompassEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(false);

        this.switchFirstFloor.setEnabled(true);
        this.switchSecondFloor.setEnabled(true);
        this.switch22ndFloor.setEnabled(true);
    }

    /**
     * Clear the map and draw the first floor map.
     */
    @OnClick(R.id.map_control_first_floor)
    public void showFirstFloor()
    {
        this.resetMap();
        this.switchFirstFloor.setEnabled(false);
        this.getMap().addGroundOverlay(HotelMapPoints.getFirstFloorOverlay());
    }

    /**
     * Clear the map and draw the second floor map.
     */
    @OnClick(R.id.map_control_second_floor)
    public void showSecondFloor()
    {
        this.resetMap();
        this.switchSecondFloor.setEnabled(false);
        this.getMap().addGroundOverlay(HotelMapPoints.getSecondFloorOverlay());
    }

    /**
     * Clear the map and draw the 22nd floor map.
     */
    @OnClick(R.id.map_control_22nd_floor)
    public void show22ndFloor()
    {
        this.resetMap();
        this.switch22ndFloor.setEnabled(false);
        this.getMap().addGroundOverlay(HotelMapPoints.get22ndFloorOverlay());
    }
}
