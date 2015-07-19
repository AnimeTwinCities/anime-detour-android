/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.analytics;

import com.animedetour.api.guest.model.Guest;
import com.animedetour.api.sched.model.Event;
import monolog.handler.analytics.TrackedEvent;

/**
 * Generates new event tracking objects for consistent use throughout the
 * application.
 *
 * These tracked events are designed to be passed to a log for tracing.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
final public class EventFactory
{
    public static TrackedEvent eventDetails(Event target)
    {
        return new TrackedEvent("Event", "View Details", target.getName());
    }

    public static TrackedEvent guestDetails(Guest target)
    {
        return new TrackedEvent("Guest", "View Details", target.getFullName());
    }

    public static TrackedEvent favoriteEvent(Event target)
    {
        return new TrackedEvent("Event", "Favorite", target.getName());
    }

    public static TrackedEvent suggestedClick(Event target)
    {
        return new TrackedEvent("Home", "Suggested Click", target.getName());
    }

    public static TrackedEvent notificationSetting(boolean enabled)
    {
        long value = enabled ? 1 : 0;
        return new TrackedEvent("Settings", "Notifications", null, value);
    }

    public static TrackedEvent developerEnabled()
    {
        return new TrackedEvent("Settings", "Dev-enable");
    }
}
