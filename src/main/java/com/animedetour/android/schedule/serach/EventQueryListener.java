/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.schedule.serach;

import android.support.v7.widget.SearchView;
import com.animedetour.android.database.event.EventRepository;
import com.animedetour.api.sched.model.Event;
import rx.Observer;

import java.util.List;

/**
 * Listens for updates to the search query and searches the event data based on
 * what has been entered by the user.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
public class EventQueryListener implements SearchView.OnQueryTextListener
{
    final private EventRepository eventRepository;
    final private Observer<List<Event>> resultObserver;

    public EventQueryListener(
        EventRepository eventRepository,
        Observer<List<Event>> resultObserver
    ) {
        this.eventRepository = eventRepository;
        this.resultObserver = resultObserver;
    }

    @Override
    public boolean onQueryTextSubmit(String s)
    {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String userQuery)
    {
        this.eventRepository.findMatching(userQuery, this.resultObserver);

        return true;
    }
}
