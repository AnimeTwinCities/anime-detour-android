/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2014-2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.schedule;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import com.animedetour.android.R;
import com.animedetour.android.database.event.EventRepository;
import com.animedetour.android.framework.BaseFragment;
import com.squareup.otto.Bus;
import monolog.LogName;
import org.joda.time.DateTime;
import prism.framework.DisplayName;
import prism.framework.Layout;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

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
    @Bind(R.id.event_days)
    ViewPager pager;

    @Inject
    Bus applicationBus;

    @Inject
    EventRepository eventData;

    @Inject
    EventFilterUpdater filterUpdater;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        DaysPagerAdapter pagerAdapter = new DaysPagerAdapter(
            this.getActivity(),
            this.getChildFragmentManager(),
            this.getDays()
        );
        this.pager.setAdapter(pagerAdapter);
        this.eventData.findAll(this.filterUpdater);
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
        days.add(new DateTime("2015-03-27"));
        days.add(new DateTime("2015-03-28"));
        days.add(new DateTime("2015-03-29"));

        return days;
    }
}
