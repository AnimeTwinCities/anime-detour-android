/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package org.animetwincities.animedetour.schedule.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import inkapplicaitons.android.logger.Logger;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.animetwincities.animedetour.framework.AppConfig;
import org.animetwincities.animedetour.framework.auth.AuthRepository;
import org.animetwincities.animedetour.schedule.Event;
import org.animetwincities.animedetour.schedule.ScheduleRepository;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Schedules or cancels notifications alarm for the start-time of an event.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
@Singleton
public class EventNotificationManager
{
    final private Logger logger;
    final private AppConfig preferenceManager;
    final private ScheduleRepository favoriteData;
    final private AuthRepository authRepository;
    final private AlarmManager alarmManager;
    final private Context context;

    @Inject
    public EventNotificationManager(
        Logger logger,
        AppConfig preferenceManager,
        ScheduleRepository favoriteData,
        AuthRepository authRepository,
        AlarmManager alarmManager,
        Context context
    ) {
        this.logger = logger;
        this.preferenceManager = preferenceManager;
        this.favoriteData = favoriteData;
        this.authRepository = authRepository;
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

        LocalDateTime notificationTime = event.getStart().minusMinutes(15);
        if (notificationTime.isBefore(LocalDateTime.now())) {
            return;
        }

        long triggerTime = notificationTime.atZone(ZoneId.of("-0500")).toEpochSecond() * 1000;
        this.logger.trace("Scheduling event notification for %s: %s ", triggerTime, event);
        PendingIntent intent = this.getEventIntent(event.getId());
        this.alarmManager.set(AlarmManager.RTC, triggerTime, intent);
    }

    public Disposable scheduleNotification(String event)
    {
        return favoriteData.observeEvent(event)
            .take(1)
            .subscribe(this::scheduleNotification, error -> logger.error(error, "Problem loading event to be favorited"));
    }

    /**
     * Schedules or cancels all of the users favorite event notifications.
     *
     * @param enableNotifications whether the notifications should be enabled.
     */
    public Disposable toggleNotifications(boolean enableNotifications)
    {
        if (enableNotifications) {
            return this.scheduleAllNotifications();
        } else {
            return this.cancelAllNotifications();
        }
    }

    /**
     * Cancels all notifications for each of the users favorited events.
     */
    public Disposable cancelAllNotifications()
    {
        return favoriteData.observeFavorites(authRepository.getCurrentUser().getId())
            .take(1)
            .subscribe(this::cancellAll, error -> logger.error(error, "Problem loading events to cancel"));
    }

    /**
     * Schedules a notification for each of the users favorited events.
     */
    public Disposable scheduleAllNotifications()
    {
        return favoriteData.observeFavorites(authRepository.getCurrentUser().getId())
            .take(1)
            .observeOn(Schedulers.computation())
            .flatMap(favoriteList -> favoriteData.observeEvents()
                .take(1)
                .observeOn(Schedulers.computation())
                .flatMap(events -> Observable.fromIterable(events)
                    .filter(event -> favoriteList.contains(event.getId()))
                    .toList()
                    .toObservable()))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::scheduleAll, error -> logger.error(error, "Problem loading events to cancel"));
    }

    private void scheduleAll(List<Event> events)
    {
        for (Event event : events) {
            scheduleNotification(event);
        }
    }

    private void cancellAll(List<String> events)
    {
        for (String event : events) {
            cancelNotification(event);
        }
    }

    /**
     * Cancels a pending notification.
     *
     * @param event The event to find the notification for - to be canceled.
     */
    public void cancelNotification(String event)
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
    protected PendingIntent getEventIntent(String event)
    {
        Intent alarmIntent = new Intent(this.context, UpcomingEventReciever.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(UpcomingEventReciever.EXTRA_EVENT, event);
        alarmIntent.putExtra(UpcomingEventReciever.EXTRA_BUNDLE, bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
            this.context,
            event.hashCode(),
            alarmIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        );

        return pendingIntent;
    }
}
