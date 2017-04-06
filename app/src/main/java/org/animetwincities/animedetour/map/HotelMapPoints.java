/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2014-2016 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package org.animetwincities.animedetour.map;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import org.animetwincities.animedetour.R;

/**
 * Hotel Polygons & Marker Points Storage.
 *
 * These were taken from points in Google's MapEngine and converted. Ideally
 * we could use the XML straight from Google MapsEngine and parse it, but
 * there's currently no good API for that. Since the building doesn't change
 * often, this should be fine.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
final class HotelMapPoints
{
    /**
     * Center point of the hotel to base overlay images on and to focus the map on.
     */
    final public static LatLng HOTEL_CENTER = new LatLng(44.8619752, -93.3530438);

    /**
     * Center point of the Sheraton overflow hotel to base overlay images and
     * to focus the map on for those floors.
     */
    final public static LatLng SHERATON_CENTER = new LatLng(44.860440, -93.354369);

    /**
     * Positioning options for the first floor map image to overlay on the map.
     */
    public static GroundOverlayOptions getFirstFloorOverlay()
    {
        GroundOverlayOptions options = new GroundOverlayOptions();
        options.image(BitmapDescriptorFactory.fromResource(R.drawable.map_floor_1));
        options.position(HOTEL_CENTER, 160);
        options.bearing(180);

        return options;
    }

    /**
     * Positioning options for the second floor map image to overlay on the map.
     */
    public static GroundOverlayOptions getSecondFloorOverlay()
    {
        GroundOverlayOptions options = new GroundOverlayOptions();
        options.image(BitmapDescriptorFactory.fromResource(R.drawable.map_floor_2));
        options.position(HOTEL_CENTER, 160);
        options.bearing(180);

        return options;
    }

    /**
     * Positioning options for the 22nd floor map image to overlay on the map.
     */
    public static GroundOverlayOptions get22ndFloorOverlay()
    {
        GroundOverlayOptions options = new GroundOverlayOptions();
        options.image(BitmapDescriptorFactory.fromResource(R.drawable.map_floor_22));
        options.position(HOTEL_CENTER, 160);
        options.bearing(180);

        return options;
    }
}
