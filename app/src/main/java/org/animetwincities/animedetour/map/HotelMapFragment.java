/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2014-2016 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package org.animetwincities.animedetour.map;

import android.app.PendingIntent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import inkapplicaitons.android.logger.Logger;
import org.animetwincities.animedetour.R;
import org.animetwincities.animedetour.framework.AnimeDetourApplication;
import org.animetwincities.animedetour.framework.dependencyinjection.ApplicationComponent;

import javax.inject.Inject;

import static org.animetwincities.animedetour.map.HotelMapPoints.HOTEL_CENTER;

/**
 * Google Maps display with Hotel floor plans overlayed.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
final public class HotelMapFragment extends SupportMapFragment implements OnMapReadyCallback
{

    @BindView(R.id.map_control_first_floor)
    Button switchFirstFloor;

    @BindView(R.id.map_control_second_floor)
    Button switchSecondFloor;

    @BindView(R.id.map_control_22nd_floor)
    Button switch22ndFloor;

    @BindView(R.id.map_missing_services_view)
    LinearLayout missingGooglePlayServicesLayout;

    @Inject
    Logger logger;

    private int googlePlayServicesStatusCode;
    private GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        FrameLayout composite = new FrameLayout(this.getActivity());
        View mapView = super.onCreateView(inflater, container, savedInstanceState);
        composite.addView(mapView);

        View controlView = inflater.inflate(R.layout.map_controls, container, false);
        composite.addView(controlView);

        return composite;
    }

    /**
     * Check if the device has Google Play Services installed
     *
     * @return true if it's installed.
     */
    private boolean isGooglePlayServicesPresent() {
        this.googlePlayServicesStatusCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());
        return this.googlePlayServicesStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Hide map controls and show the informational layout for
     * installing Play Services.
     */
    private void setupUIForMissingPlayServices() {
        this.missingGooglePlayServicesLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);


        AnimeDetourApplication application = (AnimeDetourApplication) this.getActivity().getApplication();
        ApplicationComponent applicationComponent = application.getApplicationComponent();

        applicationComponent.inject(this);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!this.isGooglePlayServicesPresent()) {
            this.setupUIForMissingPlayServices();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        this.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        this.map = map;
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));
        this.resetMap(map);
        this.centerMap(map, HOTEL_CENTER, false);

        this.switchFirstFloor.setEnabled(false);
        map.addGroundOverlay(HotelMapPoints.getFirstFloorOverlay());
    }

    /**
     * Center the map camera on the hotel and zoom appropriately.
     */
    private void centerMap(GoogleMap map, LatLng location, boolean animate)
    {
        CameraUpdate camera = CameraUpdateFactory.newCameraPosition(
            new CameraPosition.Builder().target(location).zoom(18F).tilt(0).bearing(180).build()
        );

        if (animate) {
            map.animateCamera(camera);
        } else {
            map.moveCamera(camera);
        }
    }

    /**
     * Reset any overlays from the map so we can draw new floors.
     */
    private void resetMap(GoogleMap map)
    {
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
        if (null == map) {
            return;
        }

        this.resetMap(map);
        this.centerMap(map, HOTEL_CENTER, true);
        this.switchFirstFloor.setEnabled(false);
        map.addGroundOverlay(HotelMapPoints.getFirstFloorOverlay());
    }

    /**
     * Clear the map and draw the second floor map.
     */
    @OnClick(R.id.map_control_second_floor)
    public void showSecondFloor()
    {
        if (null == map) {
            return;
        }

        this.resetMap(map);
        this.centerMap(map, HOTEL_CENTER, true);
        this.switchSecondFloor.setEnabled(false);
        map.addGroundOverlay(HotelMapPoints.getSecondFloorOverlay());
    }

    /**
     * Clear the map and draw the 22nd floor map.
     */
    @OnClick(R.id.map_control_22nd_floor)
    public void show22ndFloor()
    {
        if (null == map) {
            return;
        }

        this.resetMap(map);
        this.centerMap(map, HOTEL_CENTER, true);
        this.switch22ndFloor.setEnabled(false);
        map.addGroundOverlay(HotelMapPoints.get22ndFloorOverlay());
    }

    @OnClick(R.id.get_services_button)
    public void onGetGooglePlayServicesClicked() {
        try {
            // Perform the correct action for the given status
            // code!
            if (GoogleApiAvailability.getInstance().isUserResolvableError(this.googlePlayServicesStatusCode)) {
                GoogleApiAvailability.getInstance().getErrorResolutionPendingIntent(getActivity(), this.googlePlayServicesStatusCode, 0).send();
            }
        } catch (PendingIntent.CanceledException e1) {
            // Pass
            logger.error(e1, "Play services update cancelled!");
        }
    }
}
