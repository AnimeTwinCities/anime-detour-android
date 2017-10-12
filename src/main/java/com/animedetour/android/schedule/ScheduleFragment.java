/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2014-2016 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.animedetour.android.R;
import com.animedetour.android.database.event.EventRepository;
import com.animedetour.android.framework.BaseFragment;
import com.animedetour.android.schedule.serach.EventSearchActivity;
import com.animedetour.android.settings.PreferenceManager;
import com.squareup.otto.Bus;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import monolog.LogName;
import prism.framework.DisplayName;
import prism.framework.Layout;

/**
 * Schedule fragment
 *
 * This fragment displays a pager of days, one for each day in the schedule
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
@DisplayName(R.string.schedule_title)
@LogName("Schedule")
@Layout(R.layout.schedule_pager)
final public class ScheduleFragment extends BaseFragment
{
    @BindView(R.id.event_days)
    ViewPager pager;

    @Inject
    Bus applicationBus;

    @Inject
    EventRepository eventData;

    @Inject
    PreferenceManager preferences;

    private DaysPagerAdapter pagerAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        this.pagerAdapter = new DaysPagerAdapter(
            this.getActivity(),
            this.getChildFragmentManager(),
            this.getDays()
        );
        this.pager.setAdapter(pagerAdapter);
        //Set limit according to number of days we have so that we don't ever need to fully reload
        //any fragments in the pager.
        this.pager.setOffscreenPageLimit(this.getDays().size() - 1);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.event_actions, menu);
        menu.findItem(R.id.event_actions_show_past).setChecked(this.preferences.showPastEvents());

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.event_actions_search:
                this.startActivity(new Intent(getActivity(), EventSearchActivity.class));
                return true;
            case R.id.event_actions_show_past:
                this.preferences.togglePastEvents();
                item.setChecked(this.preferences.showPastEvents());
                this.notifyVisibilityChanged();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Send a message to each sub fragment to refresh events.
     */
    private void notifyVisibilityChanged()
    {
        for (DayFragment fragment : this.pagerAdapter.getFragments()) {
            if (false == fragment.isResumed()) {
                continue;
            }
            fragment.updateEvents();
        }
    }

    /**
     * Get a list of days, one for each day in the schedule.
     *
     * @todo query the database here instead of hardcoding the days
     * @return A list of days in the schedule
     */
    final protected List<DateTime> getDays()
    {
        ArrayList<DateTime> days = new ArrayList<>();
        days.add(new DateTime("2016-04-22"));
        days.add(new DateTime("2016-04-23"));
        days.add(new DateTime("2016-04-24"));

        return days;
    }
}
