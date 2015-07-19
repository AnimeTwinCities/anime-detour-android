/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2014 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.framework.dependencyinjection.module;

import com.animedetour.android.guest.GuestDetailActivity;
import com.animedetour.android.guest.GuestIndexFragment;
import com.animedetour.android.home.HomeFragment;
import com.animedetour.android.main.MainActivity;
import com.animedetour.android.map.HotelMapFragment;
import com.animedetour.android.schedule.DayFragment;
import com.animedetour.android.schedule.EventActivity;
import com.animedetour.android.schedule.serach.EventSearchActivity;
import com.animedetour.android.schedule.favorite.FavoritesFragment;
import com.animedetour.android.schedule.ScheduleFragment;
import com.animedetour.android.settings.SettingsFragment;
import dagger.Module;

@Module(
    injects = {
        MainActivity.class,
        EventActivity.class,
        GuestDetailActivity.class,
        EventSearchActivity.class,

        HomeFragment.class,
        DayFragment.class,
        ScheduleFragment.class,
        GuestIndexFragment.class,
        FavoritesFragment.class,
        SettingsFragment.class,
        HotelMapFragment.class,
    },
    complete = false,
    library = true
)
final public class ActivityModule {}
