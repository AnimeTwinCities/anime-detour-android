/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.schedule.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.animedetour.android.database.favorite.FavoriteRepository;
import com.animedetour.android.schedule.favorite.Favorite;
import com.animedetour.android.settings.PreferenceManager;
import com.animedetour.api.sched.model.Event;
import monolog.Monolog;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.SQLException;

/**
 * Schedules or cancels notifications alarm for the start-time of an event.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
@Singleton
public class EventNotificationManager
{
    final private Monolog logger;
    final private PreferenceManager preferenceManager;
    final private FavoriteRepository favoriteData;
    final private AlarmManager alarmManager;
    final private Context context;

    @Inject
    public EventNotificationManager(
        Monolog logger,
        PreferenceManager preferenceManager,
        FavoriteRepository favoriteData,
        AlarmManager alarmManager,
        Context context
    ) {
        this.logger = logger;
        this.preferenceManager = preferenceManager;
        this.favoriteData = favoriteData;
        this.alarmManager = alarmManager;
        this.context = context;
    }

    /**
     * Schedules an alarm 15 minutes before the specified event.
     *
     * This will NOT schedule an alarm if the user has the notification
     * preference turned off.
     *
     * @param event The event to schedule an alarm for.
     */
    public void scheduleNotification(Event event)
    {
        if (false == this.preferenceManager.receiveEventNotifications()) {
            return;
        }

        DateTime notificationTime = event.getStartDateTime().minusMinutes(15);
        if (notificationTime.isBeforeNow()) {
            return;
        }

        this.logger.trace("Scheduling event notification: " + event);
        PendingIntent intent = this.getEventIntent(event);
        this.alarmManager.set(AlarmManager.RTC, notificationTime.getMillis(), intent);
    }

    /**
     * Schedules or cancels all of the users favorite event notifications.
     *
     * @param enableNotifications whether the notifications should be enabled.
     */
    public void toggleNotifications(boolean enableNotifications)
    {
        if (enableNotifications) {
            this.scheduleAllNotifications();
        } else {
            this.cancelAllNotifications();
        }
    }

    /**
     * Cancels all notifications for each of the users favorited events.
     */
    public void cancelAllNotifications()
    {
        try {
            for (Favorite favorite : this.favoriteData.getAll()) {
                if (null == favorite.getEvent()) {
                    continue;
                }
                this.cancelNotification(favorite.getEvent());
            }
        } catch (SQLException e) {
            this.logger.error("Could not look up events to schedule notifications.", e);
        }
    }

    /**
     * Schedules a notification for each of the users favorited events.
     */
    public void scheduleAllNotifications()
    {
        try {
            for (Favorite favorite : this.favoriteData.getAll()) {
                if (null == favorite.getEvent()) {
                    continue;
                }
                this.scheduleNotification(favorite.getEvent());
            }
        } catch (SQLException e) {
            this.logger.error("Could not look up events to schedule notifications.", e);
        }
    }

    /**
     * Cancels a pending notification.
     *
     * @param event The event to find the notification for - to be canceled.
     */
    public void cancelNotification(Event event)
    {
        this.logger.trace("Cancelling event notification: " + event);
        PendingIntent intent = this.getEventIntent(event);
        this.alarmManager.cancel(intent);
    }

    /**
     * Create a notification intent for scheduling or cancelling.
     *
     * @param event The event that the notification is for.
     * @return An intent that may be used to schedule or cancel a notification.
     */
    protected PendingIntent getEventIntent(Event event)
    {
        Intent alarmIntent = new Intent(this.context, UpcomingEventReciever.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(UpcomingEventReciever.EXTRA_EVENT, event);
        alarmIntent.putExtra(UpcomingEventReciever.EXTRA_BUNDLE, bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
            this.context,
            event.getId().hashCode(),
            alarmIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        );

        return pendingIntent;
    }
}
