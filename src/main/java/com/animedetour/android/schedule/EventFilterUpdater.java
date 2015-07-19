/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.schedule;

import com.animedetour.android.main.NavigationSubContentUpdate;
import com.animedetour.api.sched.model.Event;
import com.squareup.otto.Bus;
import monolog.Monolog;
import rx.Observer;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Service for sending out navigation updtates based on available event types
 * to filer on.
 *
 * This listens for updates to a collection of events, finds each of the unique
 * event types available, and sends out an event on the bus with the distinct
 * types.
 *
 * @todo replace this with a query just for the types instead of an entire collection.
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
final public class EventFilterUpdater implements Observer<List<Event>>
{
    /**
     * Tag to inject at the top of the list to use as a filter for all the events.
     *
     * @todo Localization issue here; find a way to remove this string.
     */
    final public static String ALL_EVENTS = "All Events";

    /** Event bus to dispatch the navigation content changes onto. */
    final private Bus applicationBus;

    /** Log used when requests come back with errors. */
    final private Monolog logger;

    /**
     * @param applicationBus Event bus to dispatch the navigation content changes onto.
     * @param logger Log used when requests come back with errors.
     */
    @Inject
    public EventFilterUpdater(Bus applicationBus, Monolog logger)
    {
        this.applicationBus = applicationBus;
        this.logger = logger;
    }

    @Override
    public void onNext(List<Event> events)
    {
        Set<String> types = new TreeSet<>();
        types.add(ALL_EVENTS);

        for (Event event : events) {
            if (null == event.getEventType()) {
                continue;
            }
            types.add(event.getEventType());
        }

        this.applicationBus.post(new NavigationSubContentUpdate(types));
    }

    @Override
    public void onError(Throwable e)
    {
        this.logger.error("Error loading events for filter updater", e);
    }

    @Override public void onCompleted() {}
}
