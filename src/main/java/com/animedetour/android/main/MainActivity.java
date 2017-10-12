/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2014-2016 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.main;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.animedetour.android.R;
import com.animedetour.android.framework.BaseActivity;
import com.animedetour.android.guest.GuestIndexFragment;
import com.animedetour.android.home.HomeFragment;
import com.animedetour.android.home.OffSeasonHomeFragment;
import com.animedetour.android.map.HotelMapFragment;
import com.animedetour.android.schedule.ScheduleFragment;
import com.animedetour.android.schedule.favorite.FavoritesFragment;
import com.animedetour.android.settings.SettingsFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import icepick.State;
import org.joda.time.DateTime;
import prism.framework.Layout;

/**
 * Main containing Activity
 *
 * This is the default activity of the application.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
@Layout(R.layout.main)
final public class MainActivity extends BaseActivity
{
    /**
     * Storage of the current page title.
     *
     * This is used for when the drawer is opened/closed and the title is
     * swapped with the application title
     */
    @State
    String pageTitle;

    /**
     * View of the main sliding left drawer
     */
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.main_action_bar)
    Toolbar actionBar;

    @BindView(R.id.drawer_favorites)
    View favoritesOption;

    @Inject
    DrawerControllerFactory drawerControllerFactory;

    private DrawerController drawerController;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.showSystemBarBackground();
        this.setSupportActionBar(this.actionBar);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);

        this.drawerController = this.drawerControllerFactory.create(
            this.drawer,
            this.actionBar,
            R.string.drawer_open,
            R.string.drawer_close,
            this.pageTitle,
            this.favoritesOption
        );


        if (null == savedInstanceState) {
            this.openLandingFragment();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        this.pageTitle = this.drawerController.getPageTitle();
        super.onSaveInstanceState(outState);
    }

    /**
     * Opens the Landing page in the main view
     */
    @OnClick(R.id.drawer_home)
    protected void openLandingFragment()
    {
        this.drawerController.closeToPage(HomeFragment.class);

        // @todo – Remove this hardcoded date check when the API has the capability to lookup convention dates (soon, I promise)
        if (DateTime.now().isAfter(new DateTime("2016-04-24").withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59))) {
            this.contentFragmentTransaction(new OffSeasonHomeFragment(), "home");
        } else {
            this.contentFragmentTransaction(new HomeFragment(), "home");
        }
    }

    /**
     * Opens the Schedule / Programming page in the main view
     */
    @OnClick(R.id.drawer_programming)
    protected void openScheduleFragment()
    {
        this.drawerController.closeToPage(ScheduleFragment.class);
        this.contentFragmentTransaction(new ScheduleFragment(), "schedule");
    }

    @OnClick(R.id.drawer_favorites)
    protected void openFavorites()
    {
        this.drawerController.closeToPage(FavoritesFragment.class);
        this.contentFragmentTransaction(new FavoritesFragment(), "favorites");
    }

    @OnClick(R.id.drawer_guests)
    protected void openGuests()
    {
        this.drawerController.closeToPage(GuestIndexFragment.class);
        this.contentFragmentTransaction(new GuestIndexFragment(), "guests");
    }

    @OnClick(R.id.drawer_maps)
    protected void openMaps()
    {
        this.drawerController.closeToPage(HotelMapFragment.class);
        this.contentFragmentTransaction(new HotelMapFragment(), "map");
    }

    @OnClick(R.id.drawer_settings)
    protected void openSettings()
    {
        this.drawerController.closeToPage(SettingsFragment.class);
        this.contentFragmentTransaction(new SettingsFragment(), "settings");
    }

    /**
     * Transition to the new fragment, optionally adding to the back stack.
     *
     * @param newFragment The fragment to add to the main content view
     */
    protected void contentFragmentTransaction(Fragment newFragment, String tag)
    {
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
        transaction.replace(R.id.content_frame, newFragment, tag);
        transaction.commit();
    }

    @TargetApi(21)
    private void showSystemBarBackground()
    {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
    }

    @Override
    public void onBackPressed()
    {
        //If we're on a fragment other than home, return to the home fragment.
        //If we're on the home fragment, function normally.
        Fragment homeFragment = getSupportFragmentManager().findFragmentByTag("home");
        if (homeFragment != null) {
            super.onBackPressed();
        } else {
            openLandingFragment();
        }
    }
}
